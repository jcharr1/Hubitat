/**
 * Please Note: This app is NOT released under any open-source license.
 * Please be sure to read the license agreement before installing this code.
 *
 * Copyright 2019 Andrew Parker, Parker IT North East Limited
 *
 * This software package is created and licensed by Parker IT North East Limited. A United Kingdom, Limited Company.
 *
 * This software, along with associated elements, including but not limited to online and/or electronic documentation are
 * protected by international laws and treaties governing intellectual property rights.
 *
 * This software has been licensed to you. All rights are reserved. You may use and/or modify the software.
 * You may not sublicense or distribute this software or any modifications to third parties in any way.
 *
 * You may not distribute any part of this software without the author's express permission
 *
 * By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.
 * This agreement can be found on-line at: http://hubitat.uk/software_License_Agreement.txt
 * 
 * Hubitat is the trademark and intellectual property of Hubitat Inc. 
 * Parker IT North East Limited has no formal or informal affiliations or relationships with Hubitat.
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License Agreement
 * for the specific language governing permissions and limitations under the License.
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update: 06/04/2019
 *
 *  Changes:
 *
 *
 *  V2.0.0 - Recode - Moved major functions to app
 *  V1.1.1 - debug switches not displaying changes
 *  V1.1.0 - Added HSM alert handler (this will push any HSM alert to the tile)
 *  V1.0.2 - Debug - 'Configured' column D 
 *  V1.0.1 - Debug - Typo 
 *  V1.0.0 - POC
 *
 */



definition(
    name:"Super Tile Display",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: " ",
    category: "Convenience",
	
	parent: "Cobra:Super Tile",
	
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )

preferences {
	page name: "mainPage", title: " ", install: true, uninstall: true
	page name: "Setup_Font"
	page name: "line1"
	page name: "line2"
	page name: "line3"
	page name: "line4"
	page name: "line5"
	page name: "line6"
	page name: "line7"
	page name: "line8"
}




def mainPage() {
dynamicPage(name: "mainPage") {   
preCheck()	
setDone()
section (){input "vDevice1", "device.SuperTileDisplayDevice", title: "Virtual Display Device", required: true}	
section (){input "refreshInterval", "enum", title: "Auto Refresh Interval", required: true, defaultValue: "5 Seconds", options: ["1 Seconds", "2 Seconds", "5 Seconds", "10 Seconds", "15 Seconds", "30 Seconds", "1 Minutes", "2 Minutes", "5 Minutes", "15 Minutes", "30 Minutes", "1 Hours", "3 Hours"]} 
section(){
if(state.fontsDone == true){href "Setup_Font", title:"<b>Font Configured</b>", description: ""}
if(state.fontsDone == false || state.fontsDone == null){href "Setup_Font", title:"Click here to configure Font", description: ""}
}
	section(){
if(state.line1Done == true){href "line1", title:"<b>Line 1 Configured</b>", description: ""}
if(state.line1Done == false || state.line1Done == null){href "line1", title:"Click here to configure Line 1", description: ""}
if(state.line2Done == true){href "line2", title:"<b>Line 2 Configured</b>", description: ""}
if(state.line2Done == false || state.line2Done == null){href "line2", title:"Click here to configure Line 2", description: ""}
if(state.line3Done == true){href "line3", title:"<b>Line 3 Configured</b>", description: ""}
if(state.line3Done == false || state.line3Done == null){href "line3", title:"Click here to configure Line 3", description: ""}
if(state.line4Done == true){href "line4", title:"<b>Line 4 Configured</b>", description: ""}
if(state.line4Done == false || state.line4Done == null){href "line4", title:"Click here to configure Line 4", description: ""}
if(state.line5Done == true){href "line5", title:"<b>Line 5 Configured</b>", description: ""}
if(state.line5Done == false || state.line5Done == null){href "line5", title:"Click here to configure Line 5", description: ""}
if(state.line6Done == true){href "line6", title:"<b>Line 6 Configured</b>", description: ""}
if(state.line6Done == false || state.line6Done == null){href "line6", title:"Click here to configure Line 6", description: ""}
if(state.line7Done == true){href "line7", title:"<b>Line 7 Configured</b>", description: ""}
if(state.line7Done == false || state.line7Done == null){href "line7", title:"Click here to configure Line 7", description: ""}
if(state.line8Done == true){href "line8", title:"<b>Line 8 Configured</b>", description: ""}
if(state.line8Done == false || state.line8Done == null){href "line8", title:"Click here to configure Line 8", description: ""}}
section() {input "logLevel", "enum", title: "Set Logging Level", required:true, defaultValue: "NONE", options: ["NONE", "INFO", "DEBUG & INFO"]}
section() {label title: "Enter a name for this automation", required: false}}}	
def installed() {initialize()}
def updated() {initialize()}
def initialize() {
version()
log.info "Initialised with settings: ${settings}"
logCheck()
subscribeNow() 
configRefresh()	
setFont()
sendLines() 
}
def subscribeNow(){
unsubscribe()
subscribe(location, "mode", modeEventHandler)
subscribe(location, "hsmAlerts", hsmAlertHandler)
subscribe(location, "hsmStatus", hsmStatusHandler)
subscribe(vDevice1, "OverRideStatus", overRideHandler)
	
if(device1){subscribe(device1, device1attrib, device1Handler)} 
if(device2){subscribe(device2, device2attrib, device2Handler)}
if(device3){subscribe(device3, device3attrib, device3Handler)}
if(device4){subscribe(device4, device4attrib, device4Handler)}
if(device5){subscribe(device5, device5attrib, device5Handler)}
if(device6){subscribe(device6, device6attrib, device6Handler)}
if(device7){subscribe(device7, device7attrib, device7Handler)}
if(device8){subscribe(device8, device8attrib, device8Handler)}
if(device9){subscribe(device9, device9attrib, device9Handler)}
if(device10){subscribe(device10, device10attrib, device10Handler)} 
if(device11){subscribe(device11, device11attrib, device11Handler)}
if(device12){subscribe(device12, device12attrib, device12Handler)}
if(device13){subscribe(device13, device13attrib, device13Handler)}
if(device14){subscribe(device14, device14attrib, device14Handler)}
if(device15){subscribe(device15, device15attrib, device15Handler)}
if(device16){subscribe(device16, device16attrib, device16Handler)}
if(device17){subscribe(device17, device17attrib, device17Handler)}
if(device18){subscribe(device18, device18attrib, device18Handler)}
if(device19){subscribe(device19, device19attrib, device19Handler)}
if(device20){subscribe(device20, device20attrib, device20Handler)} 
if(device21){subscribe(device21, device21attrib, device21Handler)}
if(device22){subscribe(device22, device22attrib, device22Handler)}
if(device23){subscribe(device23, device23attrib, device23Handler)}
if(device24){subscribe(device24, device24attrib, device24Handler)}
if(device25){subscribe(device25, device25attrib, device25Handler)}
if(device26){subscribe(device26, device26attrib, device26Handler)}
if(device27){subscribe(device27, device27attrib, device27Handler)}
if(device28){subscribe(device28, device28attrib, device28Handler)}
if(device29){subscribe(device29, device29attrib, device29Handler)}
if(device30){subscribe(device30, device30attrib, device30Handler)}
if(device31){subscribe(device31, device31attrib, device31Handler)}
if(device32){subscribe(device32, device32attrib, device32Handler)}

}





def Setup_Font(){
	dynamicPage(name: "Setup_Font") {
		section ("Initial Font Configuration"){
			input "fweight", "enum",  title: "Font Weight", defaultValue: "Normal", required: true, options: ["Normal", "Bold"]
	   		input "fstyle", "enum",  title: "Font Style", defaultValue: "Normal", required: true, options: ["Normal", "Italic"]
			input "fcolour", "text",  title: "Font Colour (Hex Value)", defaultValue:"000000", required: true
	   		input "fsize", "number",  title: "Initial Font Size", required: true
			
		}
			
		
	}
}

def sendDash(){
LOGDEBUG("sendDash")
	if(state.overRide == false || state.overRide == null){
	LOGDEBUG(" Send Dash - state.overRide = $state.overRide")
	state.dashFormat = ""	
	state.dashFormat += "<div style='color: #$state.fc;font-size:$state.fs"
	state.dashFormat += "px;font-weight: $state.fw; $state.stl'>"
	state.dashFormat +="${state.line1aVal} ${state.line1bVal} ${state.line1cVal} ${state.line1dVal}<br>"
	state.dashFormat +="${state.line2aVal} ${state.line2bVal} ${state.line2cVal} ${state.line2dVal}<br>"
	state.dashFormat +="${state.line3aVal} ${state.line3bVal} ${state.line3cVal} ${state.line3dVal}<br>"
	state.dashFormat +="${state.line4aVal} ${state.line4bVal} ${state.line4cVal} ${state.line4dVal}<br>"
	state.dashFormat +="${state.line5aVal} ${state.line5bVal} ${state.line5cVal} ${state.line5dVal}<br>"
	state.dashFormat +="${state.line6aVal} ${state.line6bVal} ${state.line6cVal} ${state.line6dVal}<br>"
	state.dashFormat +="${state.line7aVal} ${state.line7bVal} ${state.line7cVal} ${state.line7dVal}<br>"
	state.dashFormat +="${state.line8aVal} ${state.line8bVal} ${state.line8cVal} ${state.line8dVal}"
	state.dashFormat += "</div>"
	state.CharNumber = state.dashFormat.length()
	if(state.CharNumber > 1024){
	LOGDEBUG("Too many characters for an attribute tile")
	vDevice1.tileIn("Unable to Display<br>Please Check Character Number<br>(1024 Max)")
	}
	if(state.CharNumber < 1024 && state.dashFormat != null){
	vDevice1.tileIn(state.dashFormat)	
	}
	LOGDEBUG( "Attribute Content: $state.dashFormat")
	runIn(state.refreshInterval, sendDash)
	}
	if(state.overRide == true){LOGDEBUG( "OverRide = True")}
}

def setFont(){
dFontW()
dFontC()
dFontS()
fStyle()}
def dFontC(){state.fc = "${fcolour}"}
def dFontS(){state.fs = "${fsize}"}
def dFontW(){
if(fweight == "Normal"){state.fw = "normal"}
if(fweight == "Bold"){state.fw = "bold"	}
if(fweight == "Italic"){
state.stl = "font-style: italic"}}
def fStyle(){
if(fstyle == "Italic"){
state.stl = "font-style: italic"}
if(fstyle != "Italic"){state.stl= "font-style: normal"}}
          
def configRefresh(){
state.refreshInterval1 = (settings?.refreshInterval) 
	if(state.refreshInterval1.contains("Seconds")){
LOGDEBUG("Seconds...")
		state.refreshInterval2 = state.refreshInterval1.replaceFirst(" Seconds", "")
	state.refreshInterval3 = state.refreshInterval2.toInteger()
		state.refreshInterval = state.refreshInterval3
LOGDEBUG("state.refreshInterval = $state.refreshInterval")
	}
	
	
	if(state.refreshInterval1.contains("Minutes")){
LOGDEBUG("Minutes...")
		state.refreshInterval2 = state.refreshInterval1.replaceFirst(" Minutes", "")
	state.refreshInterval = (60* state.refreshInterval2.toInteger())
	
	}
	
		if(state.refreshInterval1.contains("Hours")){
LOGDEBUG("Hours...")
		state.refreshInterval2 = state.refreshInterval1.replaceFirst(" Hours", "")
	state.refreshInterval = (3600* state.refreshInterval2.toInteger())
	
	}
LOGDEBUG("state.refreshInterval = $state.refreshInterval")
	

	
}

def overRideHandler(evt){
	LOGDEBUG("OverRideHandler")
	state.valNow = evt.value
	LOGDEBUG("OverRideHandler - Override status = $state.valNow")
	if(state.valNow == "Stopped"){
		LOGDEBUG("Stopped")
		state.overRide = false
		sendDash()
	}
	if(state.valNow == "Running"){
		LOGDEBUG("Running...")
		state.overRide = true
	}	
}

def line1() {
dynamicPage(name: "line1") {
section ("Line 1 Configuration"){
input "line1aType", "enum", required: false, title: "Line1 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	// "Dashboard Link",
if(line1aType == "Text"){input "line1aText", "text", required: true, title: "Text to show?"	}
if(line1aType == "Device Attribute"){
input "device1", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device1attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}
if(line1aType == "Dashboard Link"){
input "dLink1a", "text", title: "URL to Dashboard", required: true, multiple: false, submitOnChange: true	
input "dLink1aText", "text", title: "Link Display Name", required: true, multiple: false}	
if(line1aType == "Image URL"){
input "imageURL1a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth1a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight1a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder1a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line1bType", "enum", required: false, title: "Line1 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line1bType == "Text"){input "line1bText", "text", required: true, title: "Text to show?"	}
if(line1bType == "Device Attribute"){
input "device2", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device2attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line1bType == "Image URL"){
input "imageURL1b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth1b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight1b", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder1b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line1cType", "enum", required: false, title: "Line1 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line1cType == "Text"){input "line1cText", "text", required: true, title: "Text to show?"	}
if(line1cType == "Device Attribute"){
input "device3", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device3attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}
if(line1cType == "Image URL"){
input "imageURL1c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth1c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight1c", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder1c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 	
input "line1dType", "enum", required: false, title: "Line1 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line1dType == "Text"){input "line1dText", "text", required: true, title: "Text to show?"	}
if(line1dType == "Device Attribute"){
input "device25", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device25attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line1dType == "Image URL"){
input "imageURL1d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth1d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight1d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder1d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false }}}}
def line2() {
dynamicPage(name: "line2") {
section ("Line 2 Configuration"){
input "line2aType", "enum", required: false, title: "Line2 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line2aType == "Text"){input "line2aText", "text", required: true, title: "Text to show?"	}
if(line2aType == "Device Attribute"){
input "device4", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device4attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}
if(line2aType == "Image URL"){
input "imageURL2a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth2a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight2a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder2a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line2bType", "enum", required: false, title: "Line2 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line2bType == "Text"){input "line2bText", "text", required: true, title: "Text to show?"	}
if(line2bType == "Device Attribute"){
input "device5", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
input "device5attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line2bType == "Image URL"){
input "imageURL2b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth2b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight2b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder2b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line2cType", "enum", required: false, title: "Line2 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line2cType == "Text"){input "line2cText", "text", required: true, title: "Text to show?"	}
if(line2cType == "Device Attribute"){
input "device6", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
input "device6attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line2cType == "Image URL"){
input "imageURL2c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth2c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight2c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder2c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line2dType", "enum", required: false, title: "Line2 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line2dType == "Text"){input "line2dText", "text", required: true, title: "Text to show?"	}
if(line2dType == "Device Attribute"){
input "device26", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device26attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line2dType == "Image URL"){
input "imageURL2d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth2d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight2d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder2d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}} 	
def line3() {
dynamicPage(name: "line3") {
section ("Line 3 Configuration"){
input "line3aType", "enum", required: false, title: "Line3 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line3aType == "Text"){input "line3aText", "text", required: true, title: "Text to show?"	}
if(line3aType == "Device Attribute"){
input "device7", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
input "device7attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line3aType == "Image URL"){
input "imageURL3a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth3a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight3a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder3a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line3bType", "enum", required: false, title: "Line3 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line3bType == "Text"){input "line3bText", "text", required: true, title: "Text to show?"	}
if(line3bType == "Device Attribute"){
input "device8", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
input "device8attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}
if(line3bType == "Image URL"){
input "imageURL3b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth3b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight3b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder3b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line3cType", "enum", required: false, title: "Line3 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	 	
if(line3cType == "Text"){input "line3cText", "text", required: true, title: "Text to show?"	}
if(line3cType == "Device Attribute"){
input "device9", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
input "device9attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line3cType == "Image URL"){
input "imageURL3c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth3c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight3c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder3c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line3dType", "enum", required: false, title: "Line3 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line3dType == "Text"){input "line3dText", "text", required: true, title: "Text to show?"	}
if(line3dType == "Device Attribute"){
input "device27", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device27attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line3dType == "Image URL"){
input "imageURL3d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth3d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight3d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder3d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}}	
def line4() {
dynamicPage(name: "line4") {
section ("Line 4 Configuration"){
input "line4aType", "enum", required: false, title: "Line4 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line4aType == "Text"){input "line4aText", "text", required: true, title: "Text to show?"	}
if(line4aType == "Device Attribute"){
input "device10", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
input "device10attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line4aType == "Image URL"){
input "imageURL4a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth4a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight4a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder4a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line4bType", "enum", required: false, title: "Line4 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line4bType == "Text"){input "line4bText", "text", required: true, title: "Text to show?"	}
if(line4bType == "Device Attribute"){
input "device11", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
input "device11attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line4bType == "Image URL"){
input "imageURL4b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth4b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight4b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder4b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line4cType", "enum", required: false, title: "Line4 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line4cType == "Text"){input "line4cText", "text", required: true, title: "Text to show?"	}
if(line4cType == "Device Attribute"){
input "device12", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
input "device12attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line4cType == "Image URL"){
input "imageURL4c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth4c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight4c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder4c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line4dType", "enum", required: false, title: "Line4 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line4dType == "Text"){input "line4dText", "text", required: true, title: "Text to show?"	}
if(line4dType == "Device Attribute"){
input "device28", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device28attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line4dType == "Image URL"){
input "imageURL4d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth4d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight4d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder4d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}} 	
def line5() {
dynamicPage(name: "line5") {
section ("Line 5 Configuration"){
input "line5aType", "enum", required: false, title: "Line5 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line5aType == "Text"){input "line5aText", "text", required: true, title: "Text to show?"	}
if(line5aType == "Device Attribute"){
input "device13", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
input "device13attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line5aType == "Image URL"){
input "imageURL5a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth5a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight5a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder5a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line5bType", "enum", required: false, title: "Line5 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line5bType == "Text"){input "line5bText", "text", required: true, title: "Text to show?"	}
if(line5bType == "Device Attribute"){
input "device14", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device14attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line5bType == "Image URL"){
input "imageURL5b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth5b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight5b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder5b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line5cType", "enum", required: false, title: "Line5 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line5cType == "Text"){input "line5cText", "text", required: true, title: "Text to show?"	}
if(line5cType == "Device Attribute"){
input "device15", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device15attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line5caType == "Image URL"){
input "imageURL5c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth5c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight5c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder5c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false }
input "line5dType", "enum", required: false, title: "Line5 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line5dType == "Text"){input "line5dText", "text", required: true, title: "Text to show?"	}
if(line5dType == "Device Attribute"){
input "device29", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device29attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line5dType == "Image URL"){
input "imageURL5d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth5d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight5d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder5d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}}	
def line6() {
dynamicPage(name: "line6") {
section ("Line 6 Configuration"){
input "line6aType", "enum", required: false, title: "Line6 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line6aType == "Text"){input "line6aText", "text", required: true, title: "Text to show?"	}
if(line6aType == "Device Attribute"){
input "device16", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device16attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line6aType == "Image URL"){
input "imageURL6a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth6a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight6a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder6a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line6bType", "enum", required: false, title: "Line6 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line6bType == "Text"){input "line6bText", "text", required: true, title: "Text to show?"	}
if(line6bType == "Device Attribute"){
input "device17", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device17attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line6bType == "Image URL"){
input "imageURL6b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth6b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight6b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder6b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line6cType", "enum", required: false, title: "Line6 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	 	
if(line6cType == "Text"){input "line6cText", "text", required: true, title: "Text to show?"	}
if(line6cType == "Device Attribute"){
input "device18", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device18attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line6cType == "Image URL"){
input "imageURL6c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth6c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight6c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder6c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line6dType", "enum", required: false, title: "Line6 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line6dType == "Text"){input "line6dText", "text", required: true, title: "Text to show?"	}
if(line6dType == "Device Attribute"){
input "device30", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device30attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line6dType == "Image URL"){
input "imageURL6d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth6d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight6d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder6d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}} 	
def line7() {
dynamicPage(name: "line7") {
section ("Line 7 Configuration"){
input "line7aType", "enum", required: false, title: "Line7 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	 	
if(line7aType == "Text"){input "line7aText", "text", required: true, title: "Text to show?"	}
if(line7aType == "Device Attribute"){
input "device19", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device19attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line7aType == "Image URL"){
input "imageURL7a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth7a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight7a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder7a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line7bType", "enum", required: false, title: "Line7 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line7bType == "Text"){input "line7bText", "text", required: true, title: "Text to show?"	}
if(line7bType == "Device Attribute"){
input "device20", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device20attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line7bType == "Image URL"){
input "imageURL7b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth7b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight7b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder7b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line7cType", "enum", required: false, title: "Line7 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line7cType == "Text"){input "line7cText", "text", required: true, title: "Text to show?"	}
if(line7cType == "Device Attribute"){
input "device21", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device21attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line7cType == "Image URL"){
input "imageURL7c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth7c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight7c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder7c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line7dType", "enum", required: false, title: "Line7 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line7dType == "Text"){input "line7dText", "text", required: true, title: "Text to show?"	}
if(line7dType == "Device Attribute"){
input "device31", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device31attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line7dType == "Image URL"){
input "imageURL7d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth7d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight7d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder7d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}} 	
def line8() {
dynamicPage(name: "line8") {
section ("Line 8 Configuration"){
input "line8aType", "enum", required: false, title: "Line8 Column A", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 		
if(line8aType == "Text"){input "line8aText", "text", required: true, title: "Text to show?"	}
if(line8aType == "Device Attribute"){
input "device22", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device22attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line8aType == "Image URL"){
input "imageURL8a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth8a", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight8a", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder8a", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line8bType", "enum", required: false, title: "Line8 Column B", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line8bType == "Text"){input "line8bText", "text", required: true, title: "Text to show?"	}
if(line8bType == "Device Attribute"){
input "device23", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device23attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line8bType == "Image URL"){
input "imageURL8b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth8b", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight8b", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder8b", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}
input "line8cType", "enum", required: false, title: "Line8 Column C", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line8cType == "Text"){input "line8cText", "text", required: true, title: "Text to show?"	}
if(line8cType == "Device Attribute"){
input "device24", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device24attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line8cType == "Image URL"){
input "imageURL8c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth8c", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight8c", "number", title: "Enter Height", required: true, multiple: false	
input "imageBorder8c", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false} 
input "line8dType", "enum", required: false, title: "Line8 Column D", submitOnChange: true, options: ["Text", "Device Attribute", "Image URL", "Blank"] 	
if(line8dType == "Text"){input "line8dText", "text", required: true, title: "Text to show?"	}
if(line8dType == "Device Attribute"){
input "device32", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
input "device32attrib", "text", title: "Enter Device Attribute", required: true, multiple: false}	
if(line8dType == "Image URL"){
input "imageURL8d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
input "imageWidth8d", "number", title: "Enter Width", required: true, multiple: false	
input "imageHeight8d", "number", title: "Enter Height", required: true, multiple: false
input "imageBorder8d", "number", title: "Enter Border Size", required: true, defaultValue: 0 , multiple: false}}}} 






def sendLines(){
LOGDEBUG("Running sendlines... Updating data in each line & sending data to the dashboard")
state.imageURL1a = "<img src='" +imageURL1a +"' width='" +imageWidth1a +"' height='" +imageHeight1a +"' border='" +imageBorder1a +"'>"
state.imageURL1b = "<img src='" +imageURL1b +"' width='" +imageWidth1b +"' height='" +imageHeight1b +"' border='" +imageBorder1b +"'>"
state.imageURL1c = "<img src='" +imageURL1c +"' width='" +imageWidth1c +"' height='" +imageHeight1c +"' border='" +imageBorder1c +"'>"
state.imageURL1d = "<img src='" +imageURL1d +"' width='" +imageWidth1d +"' height='" +imageHeight1d +"' border='" +imageBorder1d +"'>"
state.imageURL2a = "<img src='" +imageURL2a +"' width='" +imageWidth2a +"' height='" +imageHeight2a +"' border='" +imageBorder2a +"'>"
state.imageURL2b = "<img src='" +imageURL2b +"' width='" +imageWidth2b +"' height='" +imageHeight2b +"' border='" +imageBorder2b +"'>"
state.imageURL2c = "<img src='" +imageURL2c +"' width='" +imageWidth2c +"' height='" +imageHeight2c +"' border='" +imageBorder2c +"'>"
state.imageURL2d = "<img src='" +imageURL2d +"' width='" +imageWidth2d +"' height='" +imageHeight2d +"' border='" +imageBorder2d +"'>"
state.imageURL3a = "<img src='" +imageURL3a +"' width='" +imageWidth3a +"' height='" +imageHeight3a +"' border='" +imageBorder3a +"'>"
state.imageURL3b = "<img src='" +imageURL3b +"' width='" +imageWidth3b +"' height='" +imageHeight3b +"' border='" +imageBorder3b +"'>"
state.imageURL3c = "<img src='" +imageURL3c +"' width='" +imageWidth3c +"' height='" +imageHeight3c +"' border='" +imageBorder3c +"'>"
state.imageURL3d = "<img src='" +imageURL3d +"' width='" +imageWidth3d +"' height='" +imageHeight3d +"' border='" +imageBorder3d +"'>"
state.imageURL4a = "<img src='" +imageURL4a +"' width='" +imageWidth4a +"' height='" +imageHeight4a +"' border='" +imageBorder4a +"'>"
state.imageURL4b = "<img src='" +imageURL4b +"' width='" +imageWidth4b +"' height='" +imageHeight4b +"' border='" +imageBorder4b +"'>"
state.imageURL4c = "<img src='" +imageURL4c +"' width='" +imageWidth4c +"' height='" +imageHeight4c +"' border='" +imageBorder4c +"'>"
state.imageURL4d = "<img src='" +imageURL4d +"' width='" +imageWidth4d +"' height='" +imageHeight4d +"' border='" +imageBorder4d +"'>"
state.imageURL5a = "<img src='" +imageURL5a +"' width='" +imageWidth5a +"' height='" +imageHeight5a +"' border='" +imageBorder5a +"'>"
state.imageURL5b = "<img src='" +imageURL5b +"' width='" +imageWidth5b +"' height='" +imageHeight5b +"' border='" +imageBorder5b +"'>"
state.imageURL5c = "<img src='" +imageURL5c +"' width='" +imageWidth5c +"' height='" +imageHeight5c +"' border='" +imageBorder5c +"'>"
state.imageURL5d = "<img src='" +imageURL5d +"' width='" +imageWidth5d +"' height='" +imageHeight5d +"' border='" +imageBorder5d +"'>"
state.imageURL6a = "<img src='" +imageURL6a +"' width='" +imageWidth6a +"' height='" +imageHeight6a +"' border='" +imageBorder6a +"'>"
state.imageURL6b = "<img src='" +imageURL6b +"' width='" +imageWidth6b +"' height='" +imageHeight6b +"' border='" +imageBorder6b +"'>"
state.imageURL6c = "<img src='" +imageURL6c +"' width='" +imageWidth6c +"' height='" +imageHeight6c +"' border='" +imageBorder6c +"'>"
state.imageURL6d = "<img src='" +imageURL6d +"' width='" +imageWidth6d +"' height='" +imageHeight6d +"' border='" +imageBorder6d +"'>"
state.imageURL7a = "<img src='" +imageURL7a +"' width='" +imageWidth7a +"' height='" +imageHeight7a +"' border='" +imageBorder7a +"'>"
state.imageURL7b = "<img src='" +imageURL7b +"' width='" +imageWidth7b +"' height='" +imageHeight7b +"' border='" +imageBorder7b +"'>"
state.imageURL7c = "<img src='" +imageURL7c +"' width='" +imageWidth7c +"' height='" +imageHeight7c +"' border='" +imageBorder7c +"'>"
state.imageURL7d = "<img src='" +imageURL7d +"' width='" +imageWidth7d +"' height='" +imageHeight7d +"' border='" +imageBorder7d +"'>"
state.imageURL8a = "<img src='" +imageURL8a +"' width='" +imageWidth8a +"' height='" +imageHeight8a +"' border='" +imageBorder8a +"'>"
state.imageURL8b = "<img src='" +imageURL8b +"' width='" +imageWidth8b +"' height='" +imageHeight8b +"' border='" +imageBorder8b +"'>"
state.imageURL8c = "<img src='" +imageURL8c +"' width='" +imageWidth8c +"' height='" +imageHeight8c +"' border='" +imageBorder8c +"'>"
state.imageURL8d = "<img src='" +imageURL8d +"' width='" +imageWidth8d +"' height='" +imageHeight8d +"' border='" +imageBorder8d +"'>"
if(line1aType == "Blank" || line1aType == null){state.line1aVal = " "}
if(line1aType == "Text"){state.line1aVal = line1aText}
if(line1aType == "Device Attribute"){state.line1aVal = state.dev1Val}
if(line1aType == "Image URL"){state.line1aVal = state.imageURL1a}

if(line1bType == "Blank" || line1bType == null){state.line1bVal = " "}
if(line1bType == "Text"){state.line1bVal = line1bText}
if(line1bType == "Device Attribute"){state.line1bVal = state.dev2Val}
if(line1bType == "Image URL"){state.line1bVal = state.imageURL1b}
	
if(line1cType == "Blank" || line1cType == null){state.line1cVal = " "}
if(line1cType == "Text"){state.line1cVal = line1cText}
if(line1cType == "Device Attribute"){state.line1cVal = state.dev3Val}
if(line1cType == "Image URL"){state.line1cVal = state.imageURL1c}

if(line1dType == "Blank" || line1dType == null){state.line1dVal = " "}
if(line1dType == "Text"){state.line1dVal = line1dText}
if(line1dType == "Device Attribute"){state.line1dVal = state.dev25Val}
if(line1dType == "Image URL"){state.line1dVal = state.imageURL1d}
	
if(line2aType == "Blank" || line2aType == null){state.line2aVal = " "}
if(line2aType == "Text"){state.line2aVal = line2aText}
if(line2aType == "Device Attribute"){state.line2aVal = state.dev4Val}
if(line2aType == "Image URL"){state.line2aVal = state.imageURL2a}
	
if(line2bType == "Blank" || line2bType == null){state.line2bVal = " "}
if(line2bType == "Text"){state.line2bVal = line2bText}
if(line2bType == "Device Attribute"){state.line2bVal = state.dev5Val}
if(line2bType == "Image URL"){state.line2bVal = state.imageURL2b}
	
if(line2cType == "Blank" || line2cType == null){state.line2cVal = " "}
if(line2cType == "Text"){state.line2cVal = line2cText}
if(line2cType == "Device Attribute"){state.line2cVal = state.dev6Val}
if(line2cType == "Image URL"){state.line2cVal = state.imageURL2c}
	
if(line2dType == "Blank" || line2dType == null){state.line2dVal = " "}
if(line2dType == "Text"){state.line2dVal = line2dText}
if(line2dType == "Device Attribute"){state.line2dVal = state.dev26Val}
if(line2dType == "Image URL"){state.line2dVal = state.imageURL2d}
	
if(line3aType == "Blank" || line3aType == null){state.line3aVal = " "}
if(line3aType == "Text"){state.line3aVal = line3aText}
if(line3aType == "Device Attribute"){state.line3aVal = state.dev7Val}
if(line3aType == "Image URL"){state.line3aVal = state.imageURL3a}
	
if(line3bType == "Blank" || line3bType == null){state.line3bVal = " "}
if(line3bType == "Text"){state.line3bVal = line3bText}
if(line3bType == "Device Attribute"){state.line3bVal = state.dev8Val}
if(line3bType == "Image URL"){state.line3bVal = state.imageURL3b}

if(line3cType == "Blank" || line3cType == null){state.line3cVal = " "}
if(line3cType == "Text"){state.line3cVal = line3cText}
if(line3cType == "Device Attribute"){state.line3cVal = state.dev9Val}
if(line3cType == "Image URL"){state.line3cVal = state.imageURL3c}
	
if(line3dType == "Blank" || line3dType == null){state.line3dVal = " "}
if(line3dType == "Text"){state.line3dVal = line3dText}
if(line3dType == "Device Attribute"){state.line3dVal = state.dev27Val}
if(line3dType == "Image URL"){state.line3dVal = state.imageURL3d}
	
if(line4aType == "Blank" || line4aType == null){state.line4aVal = " "}
if(line4aType == "Text"){state.line4aVal = line4aText}
if(line4aType == "Device Attribute"){state.line4aVal = state.dev10Val}
if(line4aType == "Image URL"){state.line4aVal = state.imageURL4a}

if(line4bType == "Blank" || line4bType == null){state.line4bVal = " "}
if(line4bType == "Text"){state.line4bVal = line4bText}
if(line4bType == "Device Attribute"){state.line4bVal = state.dev11Val}
if(line4bType == "Image URL"){state.line4bVal = state.imageURL4b}

if(line4cType == "Blank" || line4cType == null){state.line4cVal = " "}
if(line4cType == "Text"){state.line4cVal = line4cText}
if(line4cType == "Device Attribute"){state.line4cVal = state.dev12Val}
if(line4cType == "Image URL"){state.line4cVal = state.imageURL4c}

if(line4dType == "Blank" || line4dType == null){state.line4dVal = " "}
if(line4dType == "Text"){state.line4dVal = line4dText}
if(line4dType == "Device Attribute"){state.line4dVal = state.dev28Val}
if(line4dType == "Image URL"){state.line4dVal = state.imageURL4d}
	
if(line5aType == "Blank" || line5aType == null){state.line5aVal = " "}
if(line5aType == "Text"){state.line5aVal = line5aText}
if(line5aType == "Device Attribute"){state.line5aVal = state.dev13Val}
if(line5aType == "Image URL"){state.line5aVal = state.imageURL5a}
	
if(line5bType == "Blank" || line5bType == null){state.line5bVal = " "}
if(line5bType == "Text"){state.line5bVal = line5bText}
if(line5bType == "Device Attribute"){state.line5bVal = state.dev14Val}
if(line5bType == "Image URL"){state.line5bVal = state.imageURL5b}
	
if(line5cType == "Blank" || line5cType == null){state.line5cVal = " "}
if(line5cType == "Text"){state.line5cVal = line5cText}
if(line5cType == "Device Attribute"){state.line5cVal = state.dev15Val}
if(line5cType == "Image URL"){state.line5cVal = state.imageURL5c}
	
if(line5dType == "Blank" || line5dType == null){state.line5dVal = " "}
if(line5dType == "Text"){state.line5dVal = line5dText}
if(line5dType == "Device Attribute"){state.line5dVal = state.dev29Val}
if(line5dType == "Image URL"){state.line5dVal = state.imageURL5d}
	
if(line6aType == "Blank" || line6aType == null){state.line6aVal = " "}
if(line6aType == "Text"){state.line6aVal = line6aText}
if(line6aType == "Device Attribute"){state.line6aVal = state.dev16Val}
if(line6aType == "Image URL"){state.line6aVal = state.imageURL6a}
	
if(line6bType == "Blank" || line6bType == null){state.line6bVal = " "}
if(line6bType == "Text"){state.line6bVal = line6bText}
if(line6bType == "Device Attribute"){state.line6bVal = state.dev17Val}
if(line6bType == "Image URL"){state.line6bVal = state.imageURL6b}
	
if(line6cType == "Blank" || line6cType == null){state.line6cVal = " "}
if(line6cType == "Text"){state.line6cVal = line6cText}
if(line6cType == "Device Attribute"){state.line6cVal = state.dev18Val}
if(line6cType == "Image URL"){state.line6cVal = state.imageURL6c}
	
if(line6dType == "Blank" || line6dType == null){state.line6dVal = " "}
if(line6dType == "Text"){state.line6dVal = line6dText}
if(line6dType == "Device Attribute"){state.line6dVal = state.dev30Val}
if(line6dType == "Image URL"){state.line6dVal = state.imageURL6d}
	
if(line7aType == "Blank" || line7aType == null){state.line7aVal = " "}
if(line7aType == "Text"){state.line7aVal = line7aText}
if(line7aType == "Device Attribute"){state.line7aVal = state.dev19Val}
if(line7aType == "Image URL"){state.line7aVal = state.imageURL7a}
	
if(line7bType == "Blank" || line7bType == null){state.line7bVal = " "}
if(line7bType == "Text"){state.line7bVal = line7bText}
if(line7bType == "Device Attribute"){state.line7bVal = state.dev20Val}
if(line7bType == "Image URL"){state.line7bVal = state.imageURL7b}
	
if(line7cType == "Blank" || line7cType == null){state.line7cVal = " "}
if(line7cType == "Text"){state.line7cVal = line7cText}
if(line7cType == "Device Attribute"){state.line7cVal = state.dev21Val}
if(line7cType == "Image URL"){state.line7cVal = state.imageURL7c}

if(line7dType == "Blank" || line7dType == null){state.line7dVal = " "}
if(line7dType == "Text"){state.line7dVal = line7dText}
if(line7dType == "Device Attribute"){state.line7dVal = state.dev31Val}
if(line7dType == "Image URL"){state.line7dVal = state.imageURL7d}

if(line8aType == "Blank" || line8aType == null){state.line8aVal = " "}
if(line8aType == "Text"){state.line8aVal = line8aText}
if(line8aType == "Device Attribute"){state.line8aVal = state.dev22Val}
if(line8aType == "Image URL"){state.line8aVal = state.imageURL8a}
	
if(line8bType == "Blank" || line8bType == null){state.line8bVal = " "}
if(line8bType == "Text"){state.line8bVal = line8bText}
if(line8bType == "Device Attribute"){state.line8bVal = state.dev23Val}
if(line8bType == "Image URL"){state.line8bVal = state.imageURL8b}

if(line8cType == "Blank" || line8cType == null){state.line8cVal = " "}
if(line8cType == "Text"){state.line8cVal = line8cText}
if(line8cType == "Device Attribute"){state.line8cVal = state.dev24Val}
if(line8cType == "Image URL"){state.line8cVal = state.imageURL8c}

if(line8dType == "Blank" || line8dType == null){state.line8dVal = " "}
if(line8dType == "Text"){state.line8dVal = line8dText}
if(line8dType == "Device Attribute"){state.line8dVal = state.dev32Val}
if(line8dType == "Image URL"){state.line8dVal = state.imageURL8d}
sendDash()
runIn(state.refreshInterval,sendDash)
// vDevice1.refresh()
	
}
	

def setDone(){
	state.fz = fsize
if(state.fz == null){state.fontsDone = false}	
if(state.fz != null){state.fontsDone = true}		
if(state.line1aVal == " " && state.line1bVal == " " && state.line1cVal == " " && state.line1dVal == " " ){state.line1Done = false}
else if(state.line1aVal == null && state.line1bVal == null && state.line1cVal == null && state.line1dVal == null ){state.line1Done = false}
else{state.line1Done = true}
if(state.line2aVal == " " && state.line2bVal == " " && state.line2cVal == " " && state.line2dVal == " "){state.line2Done = false}
else if(state.line2aVal == null && state.line2bVal == null && state.line2cVal == null && state.line2dVal == null){state.line2Done = false}
else{state.line2Done = true}
if(state.line3aVal == " " && state.line3bVal == " " && state.line3cVal == " " && state.line3dVal == " "){state.line3Done = false}
else if(state.line3aVal == null && state.line3bVal == null && state.line3cVal == null && state.line3dVal == null){state.line3Done = false}
else{state.line3Done = true}
if(state.line4aVal == " " && state.line4bVal == " " && state.line4cVal == " " && state.line4dVal == " "){state.line4Done = false}
else if(state.line4aVal == null && state.line4bVal == null && state.line4cVal == null && state.line4dVal == null){state.line4Done = false}
else{state.line4Done = true}
if(state.line5aVal == " " && state.line5bVal == " " && state.line5cVal == " " && state.line5dVal == " "){state.line5Done = false}
else if(state.line5aVal == null && state.line5bVal == null && state.line5cVal == null && state.line5dVal == null){state.line5Done = false}
else{state.line5Done = true}
if(state.line6aVal == " " && state.line6bVal == " " && state.line6cVal == " " && state.line6dVal == " "){state.line6Done = false}
else if(state.line6aVal == null && state.line6bVal == null && state.line6cVal == null && state.line6dVal == null){state.line6Done = false}
else{state.line6Done = true}
if(state.line7aVal == " " && state.line7bVal == " " && state.line7cVal == " " && state.line7dVal == " "){state.line7Done = false}
else if(state.line7aVal == null && state.line7bVal == null && state.line7cVal == null && state.line7dVal == null){state.line7Done = false}
else{state.line7Done = true}
if(state.line8aVal == " " && state.line8bVal == " " && state.line8cVal == " " && state.line8dVal == " "){state.line8Done = false}
else if(state.line8aVal == null && state.line8bVal == null && state.line8cVal == null && state.line8dVal == null){state.line8Done = false}
else{state.line8Done = true}
LOGINFO("state.fontsDone = $state.fontsDone")	
LOGINFO("state.line1Done = $state.line1Done")
LOGINFO("state.line2Done = $state.line2Done")
LOGINFO("state.line3Done = $state.line3Done")
LOGINFO("state.line4Done = $state.line4Done")
LOGINFO("state.line5Done = $state.line5Done")
LOGINFO("state.line6Done = $state.line6Done")
LOGINFO("state.line7Done = $state.line7Done")
LOGINFO("state.line8Done = $state.line8Done")}
def modeEventHandler(evt){
state.ofMode = evt.value
LOGDEBUG( "modeEventHandler - evt = $evt.value")
vDevice1.setCurrentMode(state.ofMode)}
def hsmStatusHandler(evt){
state.ofHSM = evt.value
LOGDEBUG( "hsmStatusHandler - evt = $evt.value")
vDevice1.setHSMstate(state.ofHSM)}
def hsmAlertHandler(evt){
state.ofHSM = evt.value
LOGDEBUG( "hsmAlertHandler - evt = $evt.value")
vDevice1.setHSMstate(state.ofHSM)}
def device1Handler(evt){
def dev1evt = evt.value
state.dev1Val = dev1evt
LOGDEBUG("$device1 = $dev1evt")
sendLines()}
def device2Handler(evt){
def dev2evt = evt.value
state.dev2Val = dev2evt
LOGDEBUG( "$device2 = $dev2evt")
sendLines()}
def device3Handler(evt){
def dev3evt = evt.value
state.dev3Val = dev3evt
LOGDEBUG("$device3 = $dev3evt")
sendLines()}
def device4Handler(evt){
def dev4evt = evt.value
state.dev4Val = dev4evt
LOGDEBUG("$device4 = $dev4evt")
sendLines()}
def device5Handler(evt){
def dev5evt = evt.value
state.dev5Val = dev5evt
LOGDEBUG("$device5 = $dev5evt")
sendLines()}
def device6Handler(evt){
def dev6evt = evt.value
state.dev6Val = dev6evt
LOGDEBUG("$device6 = $dev6evt")
sendLines()}
def device7Handler(evt){
def dev7evt = evt.value
state.dev7Val = dev7evt
LOGDEBUG("$device7 = $dev7evt")
sendLines()}
def device8Handler(evt){
def dev8evt = evt.value
state.dev8Val = dev8evt
LOGDEBUG("$device8 = $dev8evt")
sendLines()}
def device9Handler(evt){
def dev9evt = evt.value
state.dev9Val = dev9evt
LOGDEBUG("$device9 = $dev9evt")
sendLines()}
def device10Handler(evt){
def dev10evt = evt.value
state.dev10Val = dev10evt
LOGDEBUG("$device10 = $dev10evt")
sendLines()}
def device11Handler(evt){
def dev11evt = evt.value
state.dev11Val = dev11evt
LOGDEBUG("$device11 = $dev11evt")
sendLines()}
def device12Handler(evt){
def dev12evt = evt.value
state.dev12Val = dev12evt
LOGDEBUG("$device12 = $dev12evt")
sendLines()}
def device13Handler(evt){
def dev13evt = evt.value
state.dev13Val = dev13evt
LOGDEBUG("$device13 = $dev13evt")
sendLines()}
def device14Handler(evt){
def dev14evt = evt.value
state.dev14Val = dev14evt
LOGDEBUG("$device14 = $dev14evt")
sendLines()}
def device15Handler(evt){
def dev15evt = evt.value
state.dev15Val = dev15evt
LOGDEBUG("$device15 = $dev15evt")
sendLines()}
def device16Handler(evt){
def dev16evt = evt.value
state.dev16Val = dev16evt
LOGDEBUG("$device16 = $dev16evt")
sendLines()}
def device17Handler(evt){
def dev17evt = evt.value
state.dev17Val = dev17evt
LOGDEBUG("$device17 = $dev17evt")
sendLines()}
def device18Handler(evt){
def dev18evt = evt.value
state.dev18Val = dev18evt
LOGDEBUG("$device18 = $dev18evt")
sendLines()}
def device19Handler(evt){
def dev19evt = evt.value
state.dev19Val = dev19evt
LOGDEBUG("$device19 = $dev19evt")
sendLines()}
def device20Handler(evt){
def dev20evt = evt.value
state.dev20Val = dev20evt
LOGDEBUG("$device20 = $dev20evt")
sendLines()}
def device21Handler(evt){
def dev21evt = evt.value
state.dev21Val = dev21evt
LOGDEBUG("$device21 = $dev21evt")
sendLines()}
def device22Handler(evt){
def dev22evt = evt.value
state.dev22Val = dev22evt
LOGDEBUG("$device22 = $dev22evt")
sendLines()}
def device23Handler(evt){
def dev23evt = evt.value
state.dev23Val = dev23evt
LOGDEBUG("$device23 = $dev23evt")
sendLines()}
def device24Handler(evt){
def dev24evt = evt.value
state.dev24Val = dev24evt
LOGDEBUG("$device24 = $dev24evt")
sendLines()}
def device25Handler(evt){
def dev25evt = evt.value
state.dev25Val = dev25evt
LOGDEBUG("$device25 = $dev25evt")
sendLines()}
def device26Handler(evt){
def dev26evt = evt.value
state.dev26Val = dev26evt
LOGDEBUG("$device26 = $dev26evt")
sendLines()}
def device27Handler(evt){
def dev27evt = evt.value
state.dev27Val = dev27evt
LOGDEBUG("$device27 = $dev27evt")
sendLines()}
def device28Handler(evt){
def dev28evt = evt.value
state.dev28Val = dev28evt
LOGDEBUG("$device28 = $dev28evt")
sendLines()}
def device29Handler(evt){
def dev29evt = evt.value
state.dev29Val = dev29evt
LOGDEBUG("$device29 = $dev29evt")
sendLines()}
def device30Handler(evt){
def dev30evt = evt.value
state.dev30Val = dev30evt
LOGDEBUG("$device30 = $dev30evt")
sendLines()}
def device31Handler(evt){
def dev31evt = evt.value
state.dev31Val = dev31evt
LOGDEBUG("$device31 = $dev31evt")
sendLines()}
def device32Handler(evt){
def dev32evt = evt.value
state.dev32Val = dev32evt
LOGDEBUG("$device32 = $dev32evt")
sendLines()}
		










def version(){
	setDefaults()
	updateCheck()
	pauseOrNot()
	logCheck()
	resetBtnName()
	schedule("${state.checkCron}", updateCheck) //  Check for updates every Friday
	checkButtons()
	
   
}

def logsDown(){
    log.warn "Debug logging disabled... Info logging enabled"
    app.updateSetting("logLevel",[value:"INFO",type:"enum"])
	
}


def logCheck(){
    state.checkLog = logLevel
	if(state.checkLog == "INFO"){log.info "Informational Logging Enabled"}
	if(state.checkLog == "DEBUG & INFO"){log.info "Debug & Info Logging Enabled"}
	if(state.checkLog == "NONE"){log.info "Further Logging Disabled"}
	if(logLevel == "DEBUG & INFO") runIn(1800,logsDown) //1800
}


def LOGDEBUG(txt){
	if(state.checkLog == "DEBUG & INFO"){
    try {
    	log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
  }
		
}

def LOGINFO(txt){
	if(state.checkLog == "INFO" || state.checkLog == "DEBUG & INFO"){
    try {
    	log.info("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGINFO unable to output requested data!")
    }
  }
}

def display(){
    setDefaults()
   if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br>$state.Copyright"}}
    if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
    if(state.status != "Current"){section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}}
    section(){input "pause1", "bool", title: "Pause This App", required: true, submitOnChange: true, defaultValue: false }
}



def checkButtons(){
    LOGDEBUG("Running checkButtons")
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
    state.btnCall = btn
    if(state.btnCall == "updateBtn"){
    LOGDEBUG("Checking for updates now...")
    updateCheck()
    pause(3000)
    state.btnName = state.newBtn
    runIn(2, resetBtnName)
    }
    if(state.btnCall == "updateBtn1"){
    state.btnName1 = "Click Here" 
    httpGet("https://github.com/CobraVmax/Hubitat/tree/master/Apps' target='_blank")
    }
    
}   
def resetBtnName(){
    LOGDEBUG("Resetting Button")
    if(state.status != "Current"){
    state.btnName = state.newBtn
    }
    else{
    state.btnName = "Check For Update" 
    }
}    
    

def pushOverUpdate(inMsg){
    if(updateNotification == true){  
    newMessage = inMsg
    LOGDEBUG(" Message = $newMessage ")  
    state.msg1 = '[L]' + newMessage
    speakerUpdate.speak(state.msg1)
    }
}

def pauseOrNot(){
LOGDEBUG(" Calling 'pauseOrNot'...")
    state.pauseNow = pause1
    if(state.pauseNow == true){
    state.pauseApp = true
    if(app.label){
    if(app.label.contains('red')){
    LOGDEBUG( "Paused")}
    else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
    LOGDEBUG("App Paused - state.pauseApp = $state.pauseApp " )  
    }
   }
  }
    if(state.pauseNow == false){
    state.pauseApp = false
    if(app.label){
    if(app.label.contains('red')){ app.updateLabel(app.label.minus("<font color = 'red'> (Paused) </font>" ))
    LOGDEBUG("App Released - state.pauseApp = $state.pauseApp ")                          
    }
   }
  }    
}


def stopAllChildren(disableChild, msg){
	state.disableornot = disableChild
	state.message1 = msg
	LOGDEBUG(" $state.message1 - Disable app = $state.disableornot")
	state.appgo = state.disableornot
	state.restrictRun = state.disableornot
	if(state.disableornot == true){
	unsubscribe()
	}
	if(state.disableornot == false){
	subscribeNow()}

	
}

def updateCheck(){
    setVersion()
    def paramsUD = [uri: "http://update.hubitat.uk/json/${state.CobraAppCheck}"]
    try {
    httpGet(paramsUD) { respUD ->
//  log.warn " Version Checking - Response Data: ${respUD.data}"   // Troubleshooting Debug Code 
       		def copyrightRead = (respUD.data.copyright)
       		state.Copyright = copyrightRead
            def commentRead = (respUD.data.Comment)
       		state.Comment = commentRead

            def updateUri = (respUD.data.versions.UpdateInfo.GithubFiles.(state.InternalName))
            state.updateURI = updateUri   
            
            def newVerRaw = (respUD.data.versions.Application.(state.InternalName))
            state.newver = newVerRaw
            def newVer = (respUD.data.versions.Application.(state.InternalName).replace(".", ""))
       		def currentVer = state.version.replace(".", "")
      		state.UpdateInfo = (respUD.data.versions.UpdateInfo.Application.(state.InternalName))
                state.author = (respUD.data.author)
           
		if(newVer == "NLS"){
            state.status = "<b>** This app is no longer supported by $state.author  **</b>"  
             log.warn "** This app is no longer supported by $state.author **" 
            
      		}           
		else if(currentVer < newVer){
        	state.status = "<b>New Version Available ($newVerRaw)</b>"
        	log.warn "** There is a newer version of this app available  (Version: $newVerRaw) **"
        	log.warn " Update: $state.UpdateInfo "
             state.newBtn = state.status
            state.updateMsg = "There is a new version of '$state.ExternalName' available (Version: $newVerRaw)"
            
       		} 
		else{ 
      		state.status = "Current"
       		LOGDEBUG("You are using the current version of this app")
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
    if(state.status != "Current"){
		state.newBtn = state.status
		inform()
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}


def inform(){
	LOGDEBUG("An update is available - Telling the parent!")
	parent.childUpdate(true,state.updateMsg) 
}



def preCheck(){
	LOGDEBUG("Running Precheck")
	setVersion()
    state.appInstalled = app.getInstallationState()  
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "$state.preCheckMessage"}
    }
    if(state.appInstalled == 'COMPLETE'){
	LOGDEBUG(" installed ok....")
    display()   
 	}
}

def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
   }

    
def setVersion(){
		state.version = "2.0.0"	 
		state.InternalName = "SuperTileDisplay"
    	state.ExternalName = "Super Tile Display"
		state.preCheckMessage = "This app was designed to use a special Virtual Display device  to display data on a dashboard tile"
    	state.CobraAppCheck = "supertiledisplay.json"
		state.checkCron = "0 20 9 ? * FRI *"
}







	
