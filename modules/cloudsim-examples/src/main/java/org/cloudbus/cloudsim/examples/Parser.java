import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    private static DatacenterSetup parseGraphFile(String graphFile) {
        List<HostData> hosts = new ArrayList<>();
        List<VmData> vms = new ArrayList<>();
        List<CloudletData> cloudlets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(graphFile))) {
            String line;
            DatacenterCharacteristics datacenterCharacteristics = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("#") || line.isEmpty()) {
                    // Skip comments and empty lines
                    continue;
                } else if (line.startsWith("datacenter:")) {
                    // Parse data center characteristics
                    datacenterCharacteristics = parseDatacenterCharacteristics(reader);
                } else if (line.startsWith("hosts:")) {
                    // Parse host data
                    hosts = parseHostData(reader);
                } else if (line.startsWith("vms:")) {
                    // Parse VM data
                    vms = parseVmData(reader, hosts);
                } else if (line.startsWith("cloudlets:")) {
                    // Parse cloudlet data
                    cloudlets = parseCloudletData(reader, vms);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DatacenterSetup(hosts, vms, cloudlets);
    }

    private static DatacenterCharacteristics parseDatacenterCharacteristics(BufferedReader reader) throws IOException {
        // Parse data center characteristics
        // ...
        return null; // Implement this method to parse the data center characteristics
    }

    private static List<HostData> parseHostData(BufferedReader reader) throws IOException {
        List<HostData> hosts = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                // End of host data section
                break;
            } else if (line.startsWith("-")) {
                // Parse host data
                String[] parts = line.substring(2).split(",");
                int id = Integer.parseInt(parts[0].split(":")[1].trim());
                int numCpus = Integer.parseInt(parts[1].split(":")[1].trim());
                long mipsPerCpu = Long.parseLong(parts[2].split(":")[1].trim());
                long ramInMb = Long.parseLong(parts[3].split(":")[1].trim());
                long storageInMb = Long.parseLong(parts[4].split(":")[1].trim());
                long bwInMbps = Long.parseLong(parts[5].split(":")[1].trim());

                hosts.add(new HostData(id, numCpus, mipsPerCpu, ramInMb, storageInMb, bwInMbps));
            }
        }

        return hosts;
    }

    private static List<VmData> parseVmData(BufferedReader reader, List<HostData> hosts) throws IOException {
        List<VmData> vms = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                // End of VM data section
                break;
            } else if (line.startsWith("-")) {
                // Parse VM data
                String[] parts = line.substring(2).split(",");
                int id = Integer.parseInt(parts[0].split(":")[1].trim());
                int hostId = Integer.parseInt(parts[1].split(":")[1].trim());
                int numCpus = Integer.parseInt(parts[2].split(":")[1].trim());
                long mipsPerCpu = Long.parseLong(parts[3].split(":")[1].trim());
                long ramInMb = Long.parseLong(parts[4].split(":")[1].trim());
                long imageSize = Long.parseLong(parts[5].split(":")[1].trim());

                vms.add(new VmData(id, hostId, numCpus, mipsPerCpu, ramInMb, imageSize));
            }
        }

        return vms;
    }

    private static List<CloudletData> parseCloudletData(BufferedReader reader, List<VmData> vms) throws IOException {
        List<CloudletData> cloudlets = new ArrayList<>();
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                // End of cloudlet data section
                break;
            } else if (line.startsWith("-")) {
                // Parse cloudlet data
                String[] parts = line.substring(2).split(",");
                int id = Integer.parseInt(parts[0].split(":")[1].trim());
                long length = Long.parseLong(parts[1].split(":")[1].trim());
                long fileSize = Long.parseLong(parts[2].split(":")[1].trim());
                long outputSize = Long.parseLong(parts[3].split(":")[1].trim());
                int numCpus = Integer.parseInt(parts[4].split(":")[1].trim());
                long ramInMb = Long.parseLong(parts[5].split(":")[1].trim());
                long cpuLength = Long.parseLong(parts[6].split(":")[1].trim());
                int vmId = Integer.parseInt(parts[7].split(":")[1].trim());

                cloudlets.add(new CloudletData(id, length, fileSize, outputSize, numCpus, ramInMb, cpuLength, vmId));
            }
        }

        return cloudlets;
    }

    // ... (existing code)
}
