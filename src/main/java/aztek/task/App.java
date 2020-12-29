package aztek.task;

public class App {

	public static void main(String[] args) {
		Service service =  new Service();
		if (args.length < 1) {
			usage();

		} else if ("render".equals(args[0])) {
            	try {
    			service.render("src/main/resources/input/nginx.py");
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
		} else if ("deploy".equals(args[0])) {
        	    	service.deploy();
		} else
			usage();
	}

	private static void usage() {
		System.out.println("Usage: ");
		System.out.println("\tInput directory: ./input,\n\tOutput directory: ./output");
		System.out.println("\tput the service file in the input directory");
		System.out.println(
				"\tprepare credentials file to be used for Azure connect in ./credentials directory or use ENV variables");

		System.out.println(
				"\t\t-use render command\n\t\t\tto prepare Terraform configuration file(s) accordig to your service file");
		System.out.println("\t\t-use deploy command\n\t\t\tto deploy your desired service to Azure");
	}
}
