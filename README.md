# aztek-task

The program allows its users to model docker-container deployments in a (very simplistic) dynamic-code fashion. 
The program renders the dynamic code into Terraform HCL configurations on disk. 
The program able to deploy these configurations into an Azure account with minimal user intervention.

Usage:

	Input directory: ./input,
	Output directory: ./output
	
	put the service file in the input directory to be rendered to Terraform configuration
	
	prepare credentials file to be used for Azure connect in ./credentials directory or use ENV variables
	
		-use render command
		
			to prepare Terraform configuration file(s) accordig to your service file
			
		-use deploy command
		
			to deploy your desired service to Azure
      
	render command
		will render all the required resources to deliver the NGINX container into the cloud.

	deploy command
		invokes the code that goes and deploys all the previously prepared configurations.

After ​slingshot deploy​ there should exist a URL that is publicly accessible with the configured image answering to HTTP requests in the configured port.
