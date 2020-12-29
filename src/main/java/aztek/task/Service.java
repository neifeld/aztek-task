package aztek.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;

import com.microsoft.terraform.TerraformClient;
import com.microsoft.terraform.TerraformOptions;

public class Service {

private static final String COMMA = ",";
private static final String CLOSE_BRACE = "}";
private static final String OPEN_BRACE = "{";
private static final String COLON = ": ";
private static final String UTF_8 = "UTF-8";
private static final String PORT_PLACEHOLDER = "<port>";
private static final String IMAGE_PLACEHOLDER = "<image>";
public static final String OUTPUT_DIR = "./output"; //"src/main/resources/output";
public static final String TERRAFORM_INFRASTRUCTURE_TF = OUTPUT_DIR + "/infrastructure.tf"; //"src/main/resources/output/infrastructure.tf";
public static final String INFRASTRUCTURE_TEMPLATE = "src/main/resources/providers/infrastructure.tf";

	public void render(String input) throws Exception {

		FileInputStream fis = new FileInputStream(input);
		String inputString = IOUtils.toString(fis, UTF_8);

		String[] rows = inputString.substring(inputString.indexOf(OPEN_BRACE) + 1, inputString.indexOf(CLOSE_BRACE)).split(COMMA);
		String dockerImage = rows[0].substring(rows[0].indexOf(COLON) + 2);
		String port = rows[1].substring(rows[1].indexOf(COLON)+2);


		fis.close();
		fis = new FileInputStream(INFRASTRUCTURE_TEMPLATE);
		String template = IOUtils.toString(fis, UTF_8);

		String infra = template.replaceAll(IMAGE_PLACEHOLDER, dockerImage).replace(PORT_PLACEHOLDER, port.trim());

		FileWriter fileWriter = new FileWriter(TERRAFORM_INFRASTRUCTURE_TF);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.print(infra);
		printWriter.close();

		ResourceBundle bundle = ResourceBundle.getBundle("credentials/application");

		TerraformOptions options = new TerraformOptions();
		options.setArmSubscriptionId(bundle.getString("terraform.armSubscriptionId"));
		options.setArmClientId(bundle.getString("terraform.armClientId"));
		options.setArmClientSecret(bundle.getString("terraform.armClientSecret"));
		options.setArmTenantId(bundle.getString("terraform.armTenantId"));

		File wDir = new File(OUTPUT_DIR);

		try (TerraformClient client = new TerraformClient(options)) {
			client.setOutputListener(System.out::println);
			client.setErrorListener(System.err::println);

			client.setWorkingDirectory(wDir);
			client.plan().get();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void deploy() {
		System.out.println("Deploying...");
		System.out.println(". . .");
		System.out.println("Do you want to perform these actions?");
		System.out.println("Terraform will perform the actions described above.");
		System.out.println("Only 'yes' will be accepted to approve.\r\n");
		
	    Scanner scanner = new Scanner(System.in);
	    System.out.println("Enter a value:");

	    String answer = scanner.nextLine();  
	    scanner.close();
	    if("yes".equals(answer)) {
	    	System.out.println("\n. . .\n");
	    	
			ResourceBundle bundle = ResourceBundle.getBundle("credentials/application");
			TerraformOptions options = new TerraformOptions();
			options.setArmSubscriptionId(bundle.getString("terraform.armSubscriptionId"));
			options.setArmClientId(bundle.getString("terraform.armClientId"));
			options.setArmClientSecret(bundle.getString("terraform.armClientSecret"));
			options.setArmTenantId(bundle.getString("terraform.armTenantId"));

			File wDir = new File(OUTPUT_DIR);

			try (TerraformClient client = new TerraformClient(options)) {
				client.setOutputListener(System.out::println);
				client.setErrorListener(System.err::println);

				client.setWorkingDirectory(wDir);
				client.plan().get();
				client.apply().get();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}

}
