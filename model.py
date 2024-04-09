import torch
import torch.nn.functional as F
from torch_geometric.nn import GCNConv, HeteroConv, SAGEConv, global_mean_pool
from torch_geometric.loader import DataLoader


class HeteroGNN(torch.nn.Module):

    def __init__(self, workload_in_channels, vm_in_channels, host_in_channels,
                 hidden_channels, out_channels):
        super().__init__()
        self.conv1 = HeteroConv(
            {
                ('workload', 'assigned_to', 'vm'):
                GCNConv(workload_in_channels, hidden_channels),
                ('vm', 'runs_on', 'host'):
                SAGEConv((vm_in_channels, host_in_channels), hidden_channels),
            },
            aggr='sum')

        self.conv2 = HeteroConv(
            {
                ('workload', 'assigned_to', 'vm'):
                GCNConv(hidden_channels, hidden_channels),
                ('vm', 'runs_on', 'host'):
                SAGEConv(hidden_channels, hidden_channels),
            },
            aggr='sum')

        self.fc = torch.nn.Linear(hidden_channels, out_channels)

    def forward(self, x_dict, edge_index_dict, workload_batch):
        x_dict = self.conv1(x_dict, edge_index_dict)
        x_dict = {key: F.relu(x) for key, x in x_dict.items()}

        x_dict = self.conv2(x_dict, edge_index_dict)

        workload_emb = global_mean_pool(x_dict['workload'], workload_batch)

        out = self.fc(workload_emb)
        return out


class SurrogateModel(HeteroGNN):

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.criterion = torch.nn.MSELoss()
        self.optimizer = torch.optim.Adam(self.parameters())

    def fit_single_epoch(self, data_loader: DataLoader):
        self.train()
        total_loss = 0
        for batch in data_loader:
            # Get the batch data
            x_dict = batch.x_dict
            edge_index_dict = batch.edge_index_dict
            workload_batch = batch['workload'].batch
            labels = batch['workload'].y

            outputs = self(x_dict, edge_index_dict, workload_batch)
            loss = self.criterion(outputs, labels)
            self.optimizer.zero_grad()
            loss.backward()
            self.optimizer.stop()
            total_loss += loss.item()
