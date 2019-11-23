/*
 * This example is a groovy port of the jArchi CreateAppServicePattern script from gevaertw, thanks to him.
 * The original script is here : https://gist.github.com/gevaertw/540c64dec4e2b0955b6e3d4fc5eb54b1
 *
 * This script create default application pattern into the active model.  It creates the objects and relations.
 * It puts all objects into a folder per application and creates a view.
 */
package examples

println "Creating Default Application Pattern"

//Pop up for application name, and check if that is already in the model
def appName = prompt("Name of the application to create", "My Application Service");


def applicationServiceRead = appName
def applicationComponentRead = appName + " Application Component"
def applicationComponentDataRead = appName + " Application Data Component"
def appHostingServiceRead = appName + " Application hosting Service"
def dataHostingServiceRead = appName + " Data hosting Service"



// Create objects in active model
def applicationService = model.createElement("application-Service", applicationServiceRead);
def applicationComponent = model.createElement("application-Component", applicationComponentRead);
def applicationComponentData = model.createElement("application-Component", applicationComponentDataRead);
def appHostingService = model.createElement("technology-service", appHostingServiceRead);
def dataHostingService = model.createElement("technology-service", dataHostingServiceRead);

// Put objects in folder
def folder = gArchi("folder.Application").first();
def myAppFolder = folder.createFolder(appName);
myAppFolder.add (applicationService);
myAppFolder.add (applicationComponent);
myAppFolder.add (applicationComponentData);

folder = gArchi("folder.Technology & Physical").first();
myAppFolder = folder.createFolder(appName);
myAppFolder.add (appHostingService);
myAppFolder.add (dataHostingService);


// Create relations in active model
def relationship1 = model.createRelationship("realization-relationship", "", applicationComponent, applicationService);
def relationship2 = model.createRelationship("realization-relationship", "", applicationComponentData, applicationService);
def relationship3 = model.createRelationship("serving-relationship", "", appHostingService, applicationComponent);
def relationship4 = model.createRelationship("serving-relationship", "", dataHostingService, applicationComponentData);

//Create a view with the model objects
/*Personal taste: Grid size = 10 (preset) space between objects = 3 grids = 30
an object has a default length (X) of 13 and a height (Y) of 6
so on the grid next X value = 160, next Y value is 90
*/

//Objects
def archimateView = model.createArchimateView(appName + "-Deployment");
archimateView.viewpoint = "implementation_migration";
def viewpointObject1 = archimateView.add(applicationService,10,10,-1,-1);
def viewpointObject2 = archimateView.add(applicationComponent,10,100,-1,-1);
def viewpointObject3 = archimateView.add(applicationComponentData,170,100,-1,-1);
def viewpointObject4 = archimateView.add(appHostingService,10,190,-1,-1);
def viewpointObject5 = archimateView.add(dataHostingService,170,190,-1,-1);

//Relations
def connection1 = archimateView.add(relationship1, viewpointObject2, viewpointObject1);
def connection2 = archimateView.add(relationship2, viewpointObject3, viewpointObject1);
def connection3 = archimateView.add(relationship3, viewpointObject4, viewpointObject2);
def connection4 = archimateView.add(relationship4, viewpointObject5, viewpointObject3);

//Default properties that I use
applicationService.prop("Archimate lifecycle status", "Active", false);
applicationComponent.prop("Archimate lifecycle status", "Active", false);
applicationComponentData.prop("Archimate lifecycle status", "Active", false);
appHostingService.prop("Archimate lifecycle status", "Active", false);
dataHostingService.prop("Archimate lifecycle status", "Active", false);

exit();
