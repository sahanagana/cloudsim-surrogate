package org.cloudbus.cloudsim.examples.power.random;

import java.util.ArrayList;
import java.util.Random;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Cloudlet;

public class RandomizedDatacenterBroker extends DatacenterBroker{
	public ArrayList<Integer> cloudletAllocations;
	private Random rand;

    public RandomizedDatacenterBroker(String name) throws Exception {
		super(name);
		cloudletAllocations = new ArrayList<>();
		rand = new Random();
	}

	public void submitCloudletList(ArrayList<Cloudlet> cloudletList) {
		for (Cloudlet i : cloudletList) {
			int allocation = rand.nextInt(vmList.size());
			i.setVmId(allocation);
			cloudletAllocations.add(allocation);
		}
		super.submitCloudletList(cloudletList);
	}
}
