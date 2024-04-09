import csv
import torch
from torch_geometric.data import HeteroData, DataLoader


class Dataset:

    def __init__(self, paths: list[str]):
        self.paths = paths
        self.data = sum([self.parse_file(i) for i in self.paths], [])

    def parse_file(self, path: str):
        """
        Parses a file and returns
        """
        with open(path, 'r') as f:
            return [[float(j) for j in i] for i in csv.reader(f)]

    def gen_graph(self, workloads, vms, hosts, workload_vm_edges,
                  vm_host_edges) -> HeteroData:
        """
        # Define the node features for each type
        workloads = []  # Shape: [num_workloads, workload_feature_dim]
        vms = []  # Shape: [num_vms, vm_feature_dim]
        hosts = []  # Shape: [num_hosts, host_feature_dim]

        # Define the edge connections
        workload_vm_edges = []  # Shape: [2, num_workload_to_vm_edges]
        vm_host_edges = []  # Shape: [2, num_vm_to_host_edges]
        """
        # Extract the node features and edge connections for the current state
        workload_features = torch.tensor(workloads)
        vm_features = torch.tensor(vms)
        host_features = torch.tensor(hosts)
        workload_to_vm_edges = torch.tensor(workload_vm_edges)
        vm_to_host_edges = torch.tensor(vm_host_edges)

        # Create a HeteroData object for the current state
        data = HeteroData()
        data['workload'].x = workload_features
        data['vm'].x = vm_features
        data['host'].x = host_features
        data['workload', 'assigned_to', 'vm'].edge_index = workload_to_vm_edges
        data['vm', 'runs_on', 'host'].edge_index = vm_to_host_edges
        return data

    def parse_row(self, row: list[float]) -> HeteroData:
        """
        Calls gen_graph based on the properties of this row.
        """

    def to_torch_dataset(self, batch_size: int) -> DataLoader:
        return DataLoader([self.parse_row(i) for i in self.data],
                          batch_size=batch_size,
                          shuffle=True)
