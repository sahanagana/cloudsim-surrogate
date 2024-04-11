package org.cloudbus.cloudsim.examples.power.random;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.power.models.PowerModelSqrt;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.UtilizationModelStochastic;
import org.cloudbus.cloudsim.examples.power.Constants;

import org.cloudbus.cloudsim.CloudletSchedulerDynamicWorkload;
import org.cloudbus.cloudsim.VmSchedulerTimeSharedOverSubscription;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.power.PowerVm;


public class MonteCarloHelper {
	private Random rand;
	private int brokerId;
	private int cloudletsNumber;
	private int vmsNumber;  // hostsNumber + vmsNumber = number of total vms
	private int hostsNumber;
	private DatacenterBroker broker;
	private List<PowerHost> hostList;
	
	public static int peMin = 1;
	public static int peMax = 16;
	public static int mipsMin = 500;
	public static int mipsMax = 5000;
	public static int ramMin = 1024;
	public static int ramMax = 16384;
	public static int bwMin = 500000;
	public static int bwMax = 2000000;
	public static int machineMaxPowerMin = 70;
	public static int machineMaxPowerMax = 150;
	public static int machineStaticMin = 4;
	public static int machineStaticMax = 25;

	public MonteCarloHelper(int hostsNumber, int cloudletsNumber) {
		rand = new Random();
		this.hostsNumber = hostsNumber;
		this.vmsNumber = randIntInRange(0, hostsNumber * 3);
		this.cloudletsNumber = cloudletsNumber;
	}
	
	public int randIntInRange(int min, int max) {
		return rand.nextInt(max - min) + min;		
	}
	
	public List<PowerHost> createHostList() {
		hostList = new ArrayList<PowerHost>();
		for (int i = 0; i < hostsNumber; i++) {
			List<Pe> peList = new ArrayList<Pe>();
			int pes = randIntInRange(peMin, peMax);
			for (int j = 0; j < pes; j++) {
				peList.add(new Pe(j, new PeProvisionerSimple(randIntInRange(mipsMin, mipsMax))));
			}

			hostList.add(new PowerHostUtilizationHistory(
					i,
					new RamProvisionerSimple(randIntInRange(ramMin, ramMax)),
					new BwProvisionerSimple(randIntInRange(bwMin, bwMax)),
					Constants.HOST_STORAGE,
					peList,
					new VmSchedulerTimeSharedOverSubscription(peList),
					new PowerModelSqrt(
						randIntInRange(machineMaxPowerMin, machineMaxPowerMax),
						((double) randIntInRange(machineStaticMin, machineStaticMax)) / 100
					)
			));
		}
		return hostList;
	}

	public List<Vm> createVmList() {
		List<Vm> vms = new ArrayList<Vm>();
		for (int i = 0; i < vmsNumber; i++) {
			int mips = randIntInRange((int) (mipsMin / 2), mipsMax);
			Vm vm = new PowerVm(
					i,
					brokerId,
					mips,
					1,  // 1 processing element required per vm always
					randIntInRange((int) (ramMin / 2), ramMax),
					randIntInRange((int) (bwMin / 2), bwMax),
					Constants.VM_SIZE,
					1,
					"Xen",
					new CloudletSchedulerDynamicWorkload(mips, 1),
					Constants.SCHEDULING_INTERVAL);
			vm.hostId = randIntInRange(0, hostList.size());
			vms.add(vm);
		}
		return vms;
	}

	public List<Cloudlet> createCloudletList() {
		List<Cloudlet> list = new ArrayList<Cloudlet>();

		long fileSize = 300;
		long outputSize = 300;

		for (int i = 0; i < cloudletsNumber; i++) {
			Cloudlet cloudlet = null;
			cloudlet = new Cloudlet(
					i,
					randIntInRange((int) (mipsMin / 16), (int) (mipsMin / 2)),
					Constants.CLOUDLET_PES,
					fileSize,
					outputSize,
					new UtilizationModelStochastic(),
					new UtilizationModelStochastic(),
					new UtilizationModelStochastic());
			cloudlet.setUserId(brokerId);
			cloudlet.setVmId(i);
			list.add(cloudlet);
		}

		return list;
    }

	public DatacenterBroker createBroker() {
		try {
			broker = new RandomizedDatacenterBroker("Broker");
			brokerId = broker.getId();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return broker;
	}
}
