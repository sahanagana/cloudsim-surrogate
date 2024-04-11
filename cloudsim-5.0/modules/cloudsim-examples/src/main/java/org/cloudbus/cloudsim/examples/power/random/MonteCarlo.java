package org.cloudbus.cloudsim.examples.power.random;

import java.util.Calendar;
import java.util.Random;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.power.Helper;

/**
 * The example runner for the random workload.
 * 
 * If you are using any algorithms, policies or workload included in the power package please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since Jan 5, 2012
 */
public class MonteCarlo extends RandomRunner {
	public int num_vms;
	public int num_hosts;
	public int num_cloudlets;

	/**
	 * @param enableOutput
	 * @param outputToFile
	 * @param inputFolder
	 * @param outputFolder
	 * @param workload
	 * @param vmAllocationPolicy
	 * @param vmSelectionPolicy
	 * @param parameter
	 */
	public MonteCarlo(
			boolean enableOutput,
			boolean outputToFile,
			String inputFolder,
			String outputFolder,
			String workload,
			String vmSelectionPolicy,
			String parameter,
			int num_vms_min,
			int num_vms_max,
			int num_hosts_min,
			int num_hosts_max,
			int num_cloudlets_min,
			int num_cloudlets_max) {
		super(
				enableOutput,
				outputToFile,
				inputFolder,
				outputFolder,
				workload,
				"deterministic",
				vmSelectionPolicy,
				parameter);
		Random rand = new Random();
		num_vms = rand.nextInt(num_vms_max - num_vms_min) + num_vms_min;
		num_hosts = rand.nextInt(num_hosts_max - num_hosts_min) + num_hosts_min;
		num_cloudlets = rand.nextInt(num_cloudlets_max - num_cloudlets_min) + num_cloudlets_min;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cloudbus.cloudsim.examples.power.RunnerAbstract#init(java.lang.String)
	 */
	@Override
	protected void init(String inputFolder) {
		try {
			CloudSim.init(1, Calendar.getInstance(), false);
			
			MonteCarloHelper helper = new MonteCarloHelper(num_hosts, num_cloudlets);
			broker = helper.createBroker();

			hostList = helper.createHostList();
			vmList = helper.createVmList();
			cloudletList = helper.createCloudletList();
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
			System.exit(0);
		}
	}

}
