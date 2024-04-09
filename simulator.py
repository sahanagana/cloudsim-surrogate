from pathlib import Path
import multiprocessing as mp


class Simulator:

    def __init__(self, outdir: str, outfile_stem: str):
        self.outdir = Path(outdir)
        self.outfile_stem = outfile_stem
        self.current_file_idx = 0
        self.filesys_lock = mp.Lock()

    def run_sim(self):
        with self.filesys_lock:
            outfile = f"{self.outfile_stem}_{self.current_file_idx}.csv"
            self.current_file_idx += 1
        return outfile  # TODO: IMPLEMENT RUNNING SIMULATION

    def run_sims_mp(self, num: int, jobs=1, chunksize=1):
        with mp.Pool(jobs) as p:
            p.imap_unordered(self.run_sim, [None] * num, chunksize=chunksize)
