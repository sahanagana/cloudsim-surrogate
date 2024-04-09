import os
import simulator
import dataset
import model

class TrainLoop:

    def __init__(self):
        # initialize model, simulator
        self.sim = simulator.Simulator("output", "test")
        pass

    def train(self, epochs: int):
        # for loop
        for i in range(epoch):
            #run sims mp certain # of times
            run_sims_mp(1)
            # create dataset on all new files in directory
            ds = dataset.Dataset(create_file_list("output"))
            # call to torch dataset
            dloader = ds.to_torch_dataset(1)
            # call fit single epoch on torch dataset
            mod = model.HeteroGNN(1, 1, 1, 1, 1, 1)
            surrogate = model.SurrogateModel(mod)
            surrogate.fit_single_epoch(dloader)
        pass

    def create_file_list(directory_path: str):
        file_list = []
        for entry in os.listdir(directory_path):
            entry_path = os.path.join(directory_path, entry)
            file_list.append(entry_path)
        return file_list
