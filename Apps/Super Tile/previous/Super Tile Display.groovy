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
 *  Last Update: 30/05/2019
 *
 *  Changes:
 *
 *  V2.3.1 - Debug extra logging
 *  V2.3.0 - Work around for HTML in text boxes - Added optional 'special character' input settings
 *  V2.2.0 - Added logsdownagain to turn off all logging after 24 hrs
 *  V2.1.0 - Added icons
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
	page name: "iconConfig"
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
section (){input "refreshInterval", "enum", title: "Auto Refresh Interval", required: true, defaultValue: "30 Seconds", options: ["1 Seconds", "2 Seconds", "5 Seconds", "10 Seconds", "15 Seconds", "30 Seconds", "1 Minutes", "2 Minutes", "5 Minutes", "15 Minutes", "30 Minutes", "1 Hours", "3 Hours"]} 
section(){
if(state.iconsDone == true){href "iconConfig", title:"<b>Icons Configured</b>", description: ""}
if(state.iconsDone == false || state.iconsDone == null){href "iconConfig", title:"Click here to configure Icons", description: ""}

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
	if(monitor1aType == "Contact"){subscribe(deviceMonitor1a, "contact", device1aHandler)}
	if(monitor1aType == "Presence"){subscribe(deviceMonitor1a, "presence", device1aHandler)}
	if(monitor1aType == "Water"){subscribe(deviceMonitor1a, "water", device1aHandler)}
	if(monitor1aType == "Light"){subscribe(deviceMonitor1a, "switch", device1aHandler)}
	if(monitor1aType == "Lock"){subscribe(deviceMonitor1a, "lock", device1aHandler)}
	if(monitor1aType == "Switch"){subscribe(deviceMonitor1a, "switch", device1aHandler)}		
	if(monitor1bType == "Contact"){subscribe(deviceMonitor1b, "contact", device1bHandler)}
	if(monitor1bType == "Presence"){subscribe(deviceMonitor1b, "presence", device1bHandler)}
	if(monitor1bType == "Water"){subscribe(deviceMonitor1b, "water", device1bHandler)}
	if(monitor1bType == "Light"){subscribe(deviceMonitor1b, "switch", device1bHandler)}
	if(monitor1bType == "Switch"){subscribe(deviceMonitor1b, "switch", device1bHandler)}
	if(monitor1bType == "Lock"){subscribe(deviceMonitor1b, "lock", device1bHandler)}
	if(monitor1cType == "Contact"){subscribe(deviceMonitor1c, "contact", device1cHandler)}
	if(monitor1cType == "Presence"){subscribe(deviceMonitor1c, "presence", device1cHandler)}
	if(monitor1cType == "Water"){subscribe(deviceMonitor1c, "water", device1cHandler)}
	if(monitor1cType == "Light"){subscribe(deviceMonitor1c, "switch", device1cHandler)}
	if(monitor1cType == "Switch"){subscribe(deviceMonitor1c, "switch", device1cHandler)}
	if(monitor1cType == "Lock"){subscribe(deviceMonitor1c, "lock", device1cHandler)}
	if(monitor1dType == "Contact"){subscribe(deviceMonitor1d, "contact", device1dHandler)}
	if(monitor1dType == "Presence"){subscribe(deviceMonitor1d, "presence", device1dHandler)}
	if(monitor1dType == "Water"){subscribe(deviceMonitor1d, "water", device1dHandler)}
	if(monitor1dType == "Light"){subscribe(deviceMonitor1d, "switch", device1dHandler)}
	if(monitor1dType == "Switch"){subscribe(deviceMonitor1d, "switch", device1dHandler)}
	if(monitor1dType == "Lock"){subscribe(deviceMonitor1d, "lock", device1dHandler)}
	if(monitor2aType == "Contact"){subscribe(deviceMonitor2a, "contact", device2aHandler)}
	if(monitor2aType == "Presence"){subscribe(deviceMonitor2a, "presence", device2aHandler)}
	if(monitor2aType == "Water"){subscribe(deviceMonitor2a, "water", device2aHandler)}
	if(monitor2aType == "Light"){subscribe(deviceMonitor2a, "switch", device2aHandler)}
	if(monitor2aType == "Switch"){subscribe(deviceMonitor2a, "switch", device2aHandler)}
	if(monitor2aType == "Lock"){subscribe(deviceMonitor2a, "lock", device2aHandler)}
	if(monitor2bType == "Contact"){subscribe(deviceMonitor2b, "contact", device2bHandler)}
	if(monitor2bType == "Presence"){subscribe(deviceMonitor2b, "presence", device2bHandler)}
	if(monitor2bType == "Water"){subscribe(deviceMonitor2b, "water", device2bHandler)}
	if(monitor2bType == "Light"){subscribe(deviceMonitor2b, "switch", device2bHandler)}
	if(monitor2bType == "Switch"){subscribe(deviceMonitor2b, "switch", device2bHandler)}
		if(monitor2bType == "Lock"){subscribe(deviceMonitor2b, "lock", device2bHandler)}
	if(monitor2cType == "Contact"){subscribe(deviceMonitor2c, "contact", device2cHandler)}
	if(monitor2cType == "Presence"){subscribe(deviceMonitor2c, "presence", device2cHandler)}
	if(monitor2cType == "Water"){subscribe(deviceMonitor2c, "water", device2cHandler)}
	if(monitor2cType == "Light"){subscribe(deviceMonitor2c, "switch", device2cHandler)}
	if(monitor2cType == "Switch"){subscribe(deviceMonitor2c, "switch", device2cHandler)}
		if(monitor2cType == "Lock"){subscribe(deviceMonitor2c, "lock", device2cHandler)}
	if(monitor2dType == "Contact"){subscribe(deviceMonitor2d, "contact", device2dHandler)}
	if(monitor2dType == "Presence"){subscribe(deviceMonitor2d, "presence", device2dHandler)}
	if(monitor2dType == "Water"){subscribe(deviceMonitor2d, "water", device2dHandler)}
	if(monitor2dType == "Light"){subscribe(deviceMonitor2d, "switch", device2dHandler)}
	if(monitor2dType == "Switch"){subscribe(deviceMonitor2d, "switch", device2dHandler)}
		if(monitor2dType == "Lock"){subscribe(deviceMonitor2a, "lock", device2aHandler)}
	if(monitor3aType == "Contact"){subscribe(deviceMonitor3a, "contact", device3aHandler)}
	if(monitor3aType == "Presence"){subscribe(deviceMonitor3a, "presence", device3aHandler)}
	if(monitor3aType == "Water"){subscribe(deviceMonitor3a, "water", device3aHandler)}
	if(monitor3aType == "Light"){subscribe(deviceMonitor3a, "switch", device3aHandler)}
	if(monitor3aType == "Switch"){subscribe(deviceMonitor3a, "switch", device3aHandler)}
			if(monitor3aType == "Lock"){subscribe(deviceMonitor3a, "lock", device3aHandler)}
	if(monitor3bType == "Contact"){subscribe(deviceMonitor3b, "contact", device3bHandler)}
	if(monitor3bType == "Presence"){subscribe(deviceMonitor3b, "presence", device3bHandler)}
	if(monitor3bType == "Water"){subscribe(deviceMonitor3b, "water", device3bHandler)}
	if(monitor3bType == "Light"){subscribe(deviceMonitor3b, "switch", device3bHandler)}
	if(monitor3bType == "Switch"){subscribe(deviceMonitor3b, "switch", device3bHandler)}
			if(monitor3bType == "Lock"){subscribe(deviceMonitor3b, "lock", device3bHandler)}
	if(monitor3cType == "Contact"){subscribe(deviceMonitor3c, "contact", device3cHandler)}
	if(monitor3cType == "Presence"){subscribe(deviceMonitor3c, "presence", device3cHandler)}
	if(monitor3cType == "Water"){subscribe(deviceMonitor3c, "water", device3cHandler)}
	if(monitor3cType == "Light"){subscribe(deviceMonitor3c, "switch", device3cHandler)}
	if(monitor3cType == "Switch"){subscribe(deviceMonitor3c, "switch", device3cHandler)}
			if(monitor3cType == "Lock"){subscribe(deviceMonitor3c, "lock", device3cHandler)}
	if(monitor3dType == "Contact"){subscribe(deviceMonitor3d, "contact", device3dHandler)}
	if(monitor3dType == "Presence"){subscribe(deviceMonitor3d, "presence", device3dHandler)}
	if(monitor3dType == "Water"){subscribe(deviceMonitor3d, "water", device3dHandler)}
	if(monitor3dType == "Light"){subscribe(deviceMonitor3d, "switch", device3dHandler)}
	if(monitor3dType == "Switch"){subscribe(deviceMonitor3d, "switch", device3dHandler)}
			if(monitor3dType == "Lock"){subscribe(deviceMonitor3d, "lock", device3dHandler)}
	if(monitor4aType == "Contact"){subscribe(deviceMonitor4a, "contact", device4aHandler)}
	if(monitor4aType == "Presence"){subscribe(deviceMonitor4a, "presence", device4aHandler)}
	if(monitor4aType == "Water"){subscribe(deviceMonitor4a, "water", device4aHandler)}
	if(monitor4aType == "Light"){subscribe(deviceMonitor4a, "switch", device4aHandler)}
			if(monitor4aType == "Lock"){subscribe(deviceMonitor4a, "lock", device4aHandler)}
	if(monitor4aType == "Switch"){subscribe(deviceMonitor4a, "switch", device4aHandler)}		
	if(monitor4bType == "Contact"){subscribe(deviceMonitor4b, "contact", device4bHandler)}
	if(monitor4bType == "Presence"){subscribe(deviceMonitor4b, "presence", device4bHandler)}
	if(monitor4bType == "Water"){subscribe(deviceMonitor4b, "water", device4bHandler)}
	if(monitor4bType == "Light"){subscribe(deviceMonitor4b, "switch", device4bHandler)}
	if(monitor4bType == "Switch"){subscribe(deviceMonitor4b, "switch", device4bHandler)}
			if(monitor4bType == "Lock"){subscribe(deviceMonitor4b, "lock", device4bHandler)}
	if(monitor4cType == "Contact"){subscribe(deviceMonitor4c, "contact", device4cHandler)}
	if(monitor4cType == "Presence"){subscribe(deviceMonitor4c, "presence", device4cHandler)}
	if(monitor4cType == "Water"){subscribe(deviceMonitor4c, "water", device4cHandler)}
	if(monitor4cType == "Light"){subscribe(deviceMonitor4c, "switch", device4cHandler)}
	if(monitor4cType == "Switch"){subscribe(deviceMonitor4c, "switch", device4cHandler)}
				if(monitor4cType == "Lock"){subscribe(deviceMonitor4c, "lock", device4cHandler)}
	if(monitor4dType == "Contact"){subscribe(deviceMonitor4d, "contact", device4dHandler)}
	if(monitor4dType == "Presence"){subscribe(deviceMonitor4d, "presence", device4dHandler)}
	if(monitor4dType == "Water"){subscribe(deviceMonitor4d, "water", device4dHandler)}
	if(monitor4dType == "Light"){subscribe(deviceMonitor4d, "switch", device4dHandler)}
	if(monitor4dType == "Switch"){subscribe(deviceMonitor4d, "switch", device4dHandler)}
				if(monitor4dType == "Lock"){subscribe(deviceMonitor4d, "lock", device4dHandler)}
	if(monitor5aType == "Contact"){subscribe(deviceMonitor5a, "contact", device5aHandler)}
	if(monitor5aType == "Presence"){subscribe(deviceMonitor5a, "presence", device5aHandler)}
	if(monitor5aType == "Water"){subscribe(deviceMonitor5a, "water", device5aHandler)}
	if(monitor5aType == "Light"){subscribe(deviceMonitor5a, "switch", device5aHandler)}
	if(monitor5aType == "Switch"){subscribe(deviceMonitor5a, "switch", device5aHandler)}
				if(monitor5aType == "Lock"){subscribe(deviceMonitor5a, "lock", device5aHandler)}
	if(monitor5bType == "Contact"){subscribe(deviceMonitor5b, "contact", device5bHandler)}
	if(monitor5bType == "Presence"){subscribe(deviceMonitor5b, "presence", device5bHandler)}
	if(monitor5bType == "Water"){subscribe(deviceMonitor5b, "water", device5bHandler)}
	if(monitor5bType == "Light"){subscribe(deviceMonitor5b, "switch", device5bHandler)}
	if(monitor5bType == "Switch"){subscribe(deviceMonitor5b, "switch", device5bHandler)}
				if(monitor5bType == "Lock"){subscribe(deviceMonitor5b, "lock", device5bHandler)}
	if(monitor5cType == "Contact"){subscribe(deviceMonitor5c, "contact", device5cHandler)}
	if(monitor5cType == "Presence"){subscribe(deviceMonitor5c, "presence", device5cHandler)}
	if(monitor5cType == "Water"){subscribe(deviceMonitor5c, "water", device5cHandler)}
	if(monitor5cType == "Light"){subscribe(deviceMonitor5c, "switch", device5cHandler)}
	if(monitor5cType == "Switch"){subscribe(deviceMonitor5c, "switch", device5cHandler)}
				if(monitor5cType == "Lock"){subscribe(deviceMonitor5c, "lock", device5cHandler)}
	if(monitor5dType == "Contact"){subscribe(deviceMonitor5d, "contact", device5dHandler)}
	if(monitor5dType == "Presence"){subscribe(deviceMonitor5d, "presence", device5dHandler)}
	if(monitor5dType == "Water"){subscribe(deviceMonitor5d, "water", device5dHandler)}
	if(monitor5dType == "Light"){subscribe(deviceMonitor5d, "switch", device5dHandler)}
	if(monitor5dType == "Switch"){subscribe(deviceMonitor5d, "switch", device5dHandler)}
				if(monitor5dType == "Lock"){subscribe(deviceMonitor5d, "lock", device5dHandler)}
	if(monitor6aType == "Contact"){subscribe(deviceMonitor6a, "contact", device6aHandler)}
	if(monitor6aType == "Presence"){subscribe(deviceMonitor6a, "presence", device6aHandler)}
	if(monitor6aType == "Water"){subscribe(deviceMonitor6a, "water", device6aHandler)}
	if(monitor6aType == "Light"){subscribe(deviceMonitor6a, "switch", device6aHandler)}
	if(monitor6aType == "Switch"){subscribe(deviceMonitor6a, "switch", device6aHandler)}
				if(monitor6aType == "Lock"){subscribe(deviceMonitor6a, "lock", device6aHandler)}
	if(monitor6bType == "Contact"){subscribe(deviceMonitor6b, "contact", device6bHandler)}
	if(monitor6bType == "Presence"){subscribe(deviceMonitor6b, "presence", device6bHandler)}
	if(monitor6bType == "Water"){subscribe(deviceMonitor6b, "water", device6bHandler)}
	if(monitor6bType == "Light"){subscribe(deviceMonitor6b, "switch", device6bHandler)}
	if(monitor6bType == "Switch"){subscribe(deviceMonitor6b, "switch", device6bHandler)}
		if(monitor6bType == "Lock"){subscribe(deviceMonitor6b, "lock", device6bHandler)}
	if(monitor6cType == "Contact"){subscribe(deviceMonitor6c, "contact", device6cHandler)}
	if(monitor6cType == "Presence"){subscribe(deviceMonitor6c, "presence", device6cHandler)}
	if(monitor6cType == "Water"){subscribe(deviceMonitor6c, "water", device6cHandler)}
	if(monitor6cType == "Light"){subscribe(deviceMonitor6c, "switch", device6cHandler)}
	if(monitor6cType == "Switch"){subscribe(deviceMonitor6c, "switch", device6cHandler)}
		if(monitor6cType == "Lock"){subscribe(deviceMonitor6c, "lock", device6cHandler)}
	if(monitor6dType == "Contact"){subscribe(deviceMonitor6d, "contact", device6dHandler)}
	if(monitor6dType == "Presence"){subscribe(deviceMonitor6d, "presence", device6dHandler)}
	if(monitor6dType == "Water"){subscribe(deviceMonitor6d, "water", device6dHandler)}
	if(monitor6dType == "Light"){subscribe(deviceMonitor6d, "switch", device6dHandler)}
	if(monitor6dType == "Switch"){subscribe(deviceMonitor6d, "switch", device6dHandler)}
		if(monitor6dType == "Lock"){subscribe(deviceMonitor6d, "lock", device6dHandler)}
	if(monitor7aType == "Contact"){subscribe(deviceMonitor7a, "contact", device7aHandler)}
	if(monitor7aType == "Presence"){subscribe(deviceMonitor7a, "presence", device7aHandler)}
	if(monitor7aType == "Water"){subscribe(deviceMonitor7a, "water", device7aHandler)}
	if(monitor7aType == "Light"){subscribe(deviceMonitor7a, "switch", device7aHandler)}
	if(monitor7aType == "Switch"){subscribe(deviceMonitor7a, "switch", device7aHandler)}
		if(monitor7aType == "Lock"){subscribe(deviceMonitor7a, "lock", device7aHandler)}
	if(monitor7bType == "Contact"){subscribe(deviceMonitor7b, "contact", device7bHandler)}
	if(monitor7bType == "Presence"){subscribe(deviceMonitor7b, "presence", device7bHandler)}
	if(monitor7bType == "Water"){subscribe(deviceMonitor7b, "water", device7bHandler)}
	if(monitor7bType == "Light"){subscribe(deviceMonitor7b, "switch", device7bHandler)}
	if(monitor7bType == "Switch"){subscribe(deviceMonitor7b, "switch", device7bHandler)}
		if(monitor7bType == "Lock"){subscribe(deviceMonitor7b, "lock", device7bHandler)}
	if(monitor7cType == "Contact"){subscribe(deviceMonitor7c, "contact", device7cHandler)}
	if(monitor7cType == "Presence"){subscribe(deviceMonitor7c, "presence", device7cHandler)}
	if(monitor7cType == "Water"){subscribe(deviceMonitor7c, "water", device7cHandler)}
	if(monitor7cType == "Light"){subscribe(deviceMonitor7c, "switch", device7cHandler)}
	if(monitor7cType == "Switch"){subscribe(deviceMonitor7c, "switch", device7cHandler)}
		if(monitor7cType == "Lock"){subscribe(deviceMonitor7c, "lock", device7cHandler)}
	if(monitor7dType == "Contact"){subscribe(deviceMonitor7d, "contact", device7dHandler)}
	if(monitor7dType == "Presence"){subscribe(deviceMonitor7d, "presence", device7dHandler)}
	if(monitor7dType == "Water"){subscribe(deviceMonitor7d, "water", device7dHandler)}
	if(monitor7dType == "Light"){subscribe(deviceMonitor7d, "switch", device7dHandler)}
	if(monitor7dType == "Switch"){subscribe(deviceMonitor7d, "switch", device7dHandler)}
		if(monitor7dType == "Lock"){subscribe(deviceMonitor7d, "lock", device7dHandler)}
	if(monitor8aType == "Contact"){subscribe(deviceMonitor8a, "contact", device8aHandler)}
	if(monitor8aType == "Presence"){subscribe(deviceMonitor8a, "presence", device8aHandler)}
	if(monitor8aType == "Water"){subscribe(deviceMonitor8a, "water", device8aHandler)}
	if(monitor8aType == "Light"){subscribe(deviceMonitor8a, "switch", device8aHandler)}
	if(monitor8aType == "Switch"){subscribe(deviceMonitor8a, "switch", device8aHandler)}
		if(monitor8aType == "Lock"){subscribe(deviceMonitor8a, "lock", device8aHandler)}
	if(monitor8bType == "Contact"){subscribe(deviceMonitor8b, "contact", device8bHandler)}
	if(monitor8bType == "Presence"){subscribe(deviceMonitor8b, "presence", device8bHandler)}
	if(monitor8bType == "Water"){subscribe(deviceMonitor8b, "water", device8bHandler)}
	if(monitor8bType == "Light"){subscribe(deviceMonitor8b, "switch", device8bHandler)}
	if(monitor8bType == "Switch"){subscribe(deviceMonitor8b, "switch", device8bHandler)}
		if(monitor8bType == "Lock"){subscribe(deviceMonitor8b, "lock", device8bHandler)}
	if(monitor8cType == "Contact"){subscribe(deviceMonitor8c, "contact", device8cHandler)}
	if(monitor8cType == "Presence"){subscribe(deviceMonitor8c, "presence", device8cHandler)}
	if(monitor8cType == "Water"){subscribe(deviceMonitor8c, "water", device8cHandler)}
	if(monitor8cType == "Light"){subscribe(deviceMonitor8c, "switch", device8cHandler)}
	if(monitor8cType == "Switch"){subscribe(deviceMonitor8c, "switch", device8cHandler)}
		if(monitor8cType == "Lock"){subscribe(deviceMonitor8c, "lock", device8cHandler)}
	if(monitor8dType == "Contact"){subscribe(deviceMonitor8d, "contact", device8dHandler)}
	if(monitor8dType == "Presence"){subscribe(deviceMonitor8d, "presence", device8dHandler)}
	if(monitor8dType == "Water"){subscribe(deviceMonitor8d, "water", device8dHandler)}
	if(monitor8dType == "Light"){subscribe(deviceMonitor8d, "switch", device8dHandler)}
	if(monitor8dType == "Switch"){subscribe(deviceMonitor8d, "switch", device8dHandler)}
		if(monitor8dType == "Lock"){subscribe(deviceMonitor8d, "lock", device8dHandler)}
}


def iconConfig() {
       dynamicPage(name: "iconConfig") {

	section (){
		input "contacts", "bool", title: "Configure Contact Icons", required:true, defaultValue: false, submitOnChange: true
	}
		if(contacts){
	section (){		
		input "imageOpen", "text", title: "Contact Image URL (Open)", required: true, multiple: false	
		input "imageClosed", "text", title: "Contact Image URL (Closed)", required: true, multiple: false
		input "imageContactWidth", "number", title: "Enter Width", required: true,  defaultValue: 25, multiple: false	
		input "imageContactHeight", "number", title: "Enter Height", required: true,  defaultValue: 25, multiple: false		
		
		}
	}

	section (){
		input "switches", "bool", title: "Configure Switch Icons", required:true, defaultValue: false, submitOnChange: true
	}
		   
		if(switches){
	section (){
		input "imageOn", "text", title: "Switch Image URL (On)", required: true, multiple: false
		input "imageOff", "text", title: "Switch Image URL (Off)", required: true, multiple: false
		input "imageSwitchWidth", "number", title: "Enter Width", required: true, defaultValue: 25, multiple: false		
		input "imageSwitchHeight", "number", title: "Enter Height", required: true, defaultValue: 25, multiple: false	
		
		}
	}
		
			
	section (){
		input "waters", "bool", title: "Configure Water Sensor Icons", required:true, defaultValue: false, submitOnChange: true
	}
	
		if(waters){
	section (){
		input "imageWet", "text", title: "Water Sensor Image URL (Wet)", required: true, multiple: false
		input "imageDry", "text", title: "Water Sensor Image URL (Dry)", required: true, multiple: false
		input "imageWaterWidth", "number", title: "Enter Width", required: true, defaultValue: 25, multiple: false	
		input "imageWaterHeight", "number", title: "Enter Height", required: true, defaultValue: 25, multiple: false	
		
		}
	}
			
	section (){
		input "presences", "bool", title: "Configure Presence Sensor Icons", required:true, defaultValue: false, submitOnChange: true
	}
		if(presences){
	section (){		
		input "imagePresent", "text", title: "Presence Image URL (Present)", required: true, multiple: false
		input "imageNotPresent", "text", title: "Presence Image URL (Not Present)", required: true, multiple: false
		input "imagePresentWidth", "number", title: "Enter Width", required: true, defaultValue: 25, multiple: false	
		input "imagePresentHeight", "number", title: "Enter Height", required: true, defaultValue: 25, multiple: false	
		
		}	
	}
		   
	section (){
		input "lights", "bool", title: "Configure Light Icons", required:true, defaultValue: false, submitOnChange: true
	}
		if(lights){
	section (){
		input "imageLightOn", "text", title: "Light Image URL (On)", required: true, multiple: false
		input "imageLightOff", "text", title: "Light Image URL (Off)", required: true, multiple: false
		input "imageLightWidth", "number", title: "Enter Width", required: true, defaultValue: 25, multiple: false	
		input "imageLightHeight", "number", title: "Enter Height", required: true, defaultValue: 25, multiple: false	
		
		}	
	}	   
	 
	section (){
		input "locks", "bool", title: "Configure Lock Icons", required:true, defaultValue: false, submitOnChange: true  // soon.......
	}
		if(locks){
	section (){
		input "imageLockLocked", "text", title: "Lock Image URL (Locked)", required: true, multiple: false
		input "imageLockUnlocked", "text", title: "Lock Image URL (Unlocked)", required: true, multiple: false
		input "imageLockWidth", "number", title: "Enter Width", required: true, defaultValue: 25, multiple: false	
		input "imageLockHeight", "number", title: "Enter Height", required: true, defaultValue: 25, multiple: false	
		
		}	
	}	   	   
	
  }
}


def Setup_Font(){
	dynamicPage(name: "Setup_Font") {
		section ("Initial Font Configuration"){
			input "fweight", "enum",  title: "Font Weight", defaultValue: "Normal", required: true, options: ["Normal", "Bold"]
	   		input "fstyle", "enum",  title: "Font Style", defaultValue: "Normal", required: true, options: ["Normal", "Italic"]
			input "fcolour", "text",  title: "Font Colour (Hex Value)", defaultValue:"000000", required: true
	   		input "fsize", "number",  title: "Initial Font Size", required: true
			
		}
		
		section("Special HTML Character Entry"){
			input "specialCharacter", "bool", title: "Configure Alternative Special HTML Character", required:true, defaultValue: false, submitOnChange: true  
			if(specialCharacter){
			input "special1", "text",  title: "Special Character(s) For the '<' symbol", defaultValue:"{", required: false	
			input "special2", "text",  title: "Special Character(s) For the '>' symbol", defaultValue:"}", required: false
			}
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
	convertHtml()
	state.CharNumber = state.dashFormat.length()
	if(state.CharNumber > 1024){
	LOGDEBUG("Too many characters for an attribute tile - $state.CharNumber")
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



def convertHtml(){
	state.special1 = special1
	state.special2 = special2
	if(state.special1 == null) {state.special1 = "{"}
	if(state.special2 == null) {state.special2 = "}"}					  
	state.openChar = "<"
	state.closeChar = ">"
	if(state.dashFormat.contains(state.special1)){state.dashFormat = state.dashFormat.replace(state.special1, state.openChar )}
	if(state.dashFormat.contains(state.special2)){state.dashFormat = state.dashFormat.replace(state.special2, state.closeChar)}
	LOGDEBUG("New format = $state.dashFormat")
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
def checkLineType(){
    listInput1 = [
	
		"Blank", 
		"Device Attribute", 
		"Device Icon", 
		"Image URL", 
		"Text"
]  
    
    return listInput1
}


def checkMonitorType(){
    listInput2 = [
	
		"Contact",
		"Presence", 
		"Water", 
		"Light",
		"Lock",
		"Switch"
]  
    
    return listInput2
}


   

def line1() {
       dynamicPage(name: "line1") {

	section ("Line 1 Configuration"){
			   
	input "line1aType", "enum", required: false, title: "Line1 Column A", submitOnChange: true, options: checkLineType() 	
		if(line1aType == "Text"){input "line1aText", "text", required: true, title: "Text to show?"	}
		if(line1aType == "Device Attribute"){
		input "device1", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device1attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line1aType == "Image URL"){
		input "imageURL1a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth1a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight1a", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line1aType == "Device Icon"){
		input "monitor1aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor1a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}

	input "line1bType", "enum", required: false, title: "Line1 Column B", submitOnChange: true, options: checkLineType()
		if(line1bType == "Text"){input "line1bText", "text", required: true, title: "Text to show?"	}
		if(line1bType == "Device Attribute"){
		input "device2", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device2attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}	
		if(line1bType == "Image URL"){
		input "imageURL1b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth1b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight1b", "number", title: "Enter Height", required: true, multiple: false
		
		}	
		if(line1bType == "Device Icon"){
		input "monitor1bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor1b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	input "line1cType", "enum", required: false, title: "Line1 Column C", submitOnChange: true, options: checkLineType()	
		if(line1cType == "Text"){input "line1cText", "text", required: true, title: "Text to show?"	}
		if(line1cType == "Device Attribute"){
		input "device3", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device3attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line1cType == "Image URL"){
		input "imageURL1c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth1c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight1c", "number", title: "Enter Height", required: true, multiple: false
		
		}
		if(line1cType == "Device Icon"){
		input "monitor1cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor1c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	input "line1dType", "enum", required: false, title: "Line1 Column D", submitOnChange: true, options: checkLineType()	
		if(line1dType == "Text"){input "line1dText", "text", required: true, title: "Text to show?"	}
		if(line1dType == "Device Attribute"){
		input "device25", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device25attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line1dType == "Image URL"){
		input "imageURL1d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth1d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight1d", "number", title: "Enter Height", required: true, multiple: false
		
		}
		if(line1dType == "Device Icon"){
		input "monitor1dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor1d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
		
		
	 }	
		   
		   

  }  
}
def line2() {
       dynamicPage(name: "line2") {

	section ("Line 2 Configuration"){
			   
	input "line2aType", "enum", required: false, title: "Line2 Column A", submitOnChange: true, options: checkLineType()
		if(line2aType == "Text"){input "line2aText", "text", required: true, title: "Text to show?"	}
		if(line2aType == "Device Attribute"){
		input "device4", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device4attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line2aType == "Image URL"){
		input "imageURL2a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth2a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight2a", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line2aType == "Device Icon"){
		input "monitor2aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor2a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line2bType", "enum", required: false, title: "Line2 Column B", submitOnChange: true, options: checkLineType()	
		if(line2bType == "Text"){input "line2bText", "text", required: true, title: "Text to show?"	}
		if(line2bType == "Device Attribute"){
		input "device5", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
		input "device5attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		
		if(line2bType == "Image URL"){
		input "imageURL2b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth2b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight2b", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line2bType == "Device Icon"){
		input "monitor2bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor2b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line2cType", "enum", required: false, title: "Line2 Column C", submitOnChange: true, options: checkLineType()
		if(line2cType == "Text"){input "line2cText", "text", required: true, title: "Text to show?"	}
		if(line2cType == "Device Attribute"){
		input "device6", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
		input "device6attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line2cType == "Image URL"){
		input "imageURL2c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth2c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight2c", "number", title: "Enter Height", required: true, multiple: false	
		 
		}
		if(line2cType == "Device Icon"){
		input "monitor2cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor2c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
		
	input "line2dType", "enum", required: false, title: "Line2 Column D", submitOnChange: true, options: checkLineType()
		if(line2dType == "Text"){input "line2dText", "text", required: true, title: "Text to show?"	}
		if(line2dType == "Device Attribute"){
		input "device26", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device26attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line2dType == "Image URL"){
		input "imageURL2d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth2d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight2d", "number", title: "Enter Height", required: true, multiple: false
			
		}
		if(line2dType == "Device Icon"){
		input "monitor2dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor2d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	 }

  }  
}



def line3() {
       dynamicPage(name: "line3") {

	section ("Line 3 Configuration"){
			   
	input "line3aType", "enum", required: false, title: "Line3 Column A", submitOnChange: true, options: checkLineType()	
		if(line3aType == "Text"){input "line3aText", "text", required: true, title: "Text to show?"	}
		if(line3aType == "Device Attribute"){
		input "device7", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
		input "device7attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line3aType == "Image URL"){
		input "imageURL3a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth3a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight3a", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line3aType == "Device Icon"){
		input "monitor3aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor3a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line3bType", "enum", required: false, title: "Line3 Column B", submitOnChange: true, options: checkLineType()		
		if(line3bType == "Text"){input "line3bText", "text", required: true, title: "Text to show?"	}
		if(line3bType == "Device Attribute"){
		input "device8", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
		input "device8attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line3bType == "Image URL"){
		input "imageURL3b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth3b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight3b", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line3bType == "Device Icon"){
		input "monitor3bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor3b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
		
	input "line3cType", "enum", required: false, title: "Line3 Column C", submitOnChange: true, options: checkLineType()
		if(line3cType == "Text"){input "line3cText", "text", required: true, title: "Text to show?"	}
		if(line3cType == "Device Attribute"){
		input "device9", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
		input "device9attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line3cType == "Image URL"){
		input "imageURL3c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth3c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight3c", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line3cType == "Device Icon"){
		input "monitor3cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor3c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
		input "line3dType", "enum", required: false, title: "Line3 Column D", submitOnChange: true, options: checkLineType()	
		if(line3dType == "Text"){input "line3dText", "text", required: true, title: "Text to show?"	}
		if(line3dType == "Device Attribute"){
		input "device27", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device27attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line3dType == "Image URL"){
		input "imageURL3d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth3d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight3d", "number", title: "Enter Height", required: true, multiple: false
		
		}
		if(line3dType == "Device Icon"){
		input "monitor3dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor3d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	 }
		  
  }  
}

def line4() {
       dynamicPage(name: "line4") {
		
	section ("Line 4 Configuration"){
			   
	input "line4aType", "enum", required: false, title: "Line4 Column A", submitOnChange: true, options: checkLineType()	
		if(line4aType == "Text"){input "line4aText", "text", required: true, title: "Text to show?"	}
		if(line4aType == "Device Attribute"){
		input "device10", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
		input "device10attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line4aType == "Image URL"){
		input "imageURL4a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth4a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight4a", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line4aType == "Device Icon"){
		input "monitor4aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()
		input "deviceMonitor4a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line4bType", "enum", required: false, title: "Line4 Column B", submitOnChange: true, options: checkLineType()
		if(line4bType == "Text"){input "line4bText", "text", required: true, title: "Text to show?"	}
		if(line4bType == "Device Attribute"){
		input "device11", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
		input "device11attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line4bType == "Image URL"){
		input "imageURL4b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth4b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight4b", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line4bType == "Device Icon"){
		input "monitor4bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor4b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line4cType", "enum", required: false, title: "Line4 Column C", submitOnChange: true, options: checkLineType()
		if(line4cType == "Text"){input "line4cText", "text", required: true, title: "Text to show?"	}
		if(line4cType == "Device Attribute"){
		input "device12", "capability.*", title: "Select Device", required: true,  multiple: false, submitOnChange: true	
		input "device12attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line4cType == "Image URL"){
		input "imageURL4c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth4c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight4c", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line4cType == "Device Icon"){
		input "monitor4cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor4c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line4dType", "enum", required: false, title: "Line4 Column D", submitOnChange: true, options: checkLineType()
		if(line4dType == "Text"){input "line4dText", "text", required: true, title: "Text to show?"	}
		if(line4dType == "Device Attribute"){
		input "device28", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device28attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line4dType == "Image URL"){
		input "imageURL4d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth4d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight4d", "number", title: "Enter Height", required: true, multiple: false
		
		}
		if(line4dType == "Device Icon"){
		input "monitor4dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor4d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	 }
		   
  }  
}


def line5() {
       dynamicPage(name: "line5") {
		
	section ("Line 5 Configuration"){
			   
	input "line5aType", "enum", required: false, title: "Line5 Column A", submitOnChange: true, options: checkLineType()	
		if(line5aType == "Text"){input "line5aText", "text", required: true, title: "Text to show?"	}
		if(line5aType == "Device Attribute"){
		input "device13", "capability.*", title: "Select Device",  required: true, multiple: false, submitOnChange: true	
		input "device13attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line5aType == "Image URL"){
		input "imageURL5a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth5a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight5a", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line5aType == "Device Icon"){
		input "monitor5aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor5a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line5bType", "enum", required: false, title: "Line5 Column B", submitOnChange: true, options: checkLineType()
		if(line5bType == "Text"){input "line5bText", "text", required: true, title: "Text to show?"	}
		if(line5bType == "Device Attribute"){
		input "device14", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device14attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line5bType == "Image URL"){
		input "imageURL5b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth5b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight5b", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line5bType == "Device Icon"){
		input "monitor5bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor5b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}			
		
	input "line5cType", "enum", required: false, title: "Line5 Column C", submitOnChange: true, options: checkLineType()
		if(line5cType == "Text"){input "line5cText", "text", required: true, title: "Text to show?"	}
		if(line5cType == "Device Attribute"){
		input "device15", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device15attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line5caType == "Image URL"){
		input "imageURL5c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth5c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight5c", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line5cType == "Device Icon"){
		input "monitor5cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor5c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	input "line5dType", "enum", required: false, title: "Line5 Column D", submitOnChange: true, options: checkLineType()	
		if(line5dType == "Text"){input "line5dText", "text", required: true, title: "Text to show?"	}
		if(line5dType == "Device Attribute"){
		input "device29", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device29attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line5dType == "Image URL"){
		input "imageURL5d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth5d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight5d", "number", title: "Enter Height", required: true, multiple: false
		
		}	
		if(line5dType == "Device Icon"){
		input "monitor5dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor5d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	 }
		  
  }  
}

def line6() {
       dynamicPage(name: "line6") {
		
	section ("Line 6 Configuration"){
			   
	input "line6aType", "enum", required: false, title: "Line6 Column A", submitOnChange: true, options: checkLineType() 	
		if(line6aType == "Text"){input "line6aText", "text", required: true, title: "Text to show?"	}
		if(line6aType == "Device Attribute"){
		input "device16", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device16attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line6aType == "Image URL"){
		input "imageURL6a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth6a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight6a", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line6aType == "Device Icon"){
		input "monitor6aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType() 	
		input "deviceMonitor6a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line6bType", "enum", required: false, title: "Line6 Column B", submitOnChange: true, options: checkLineType()	
		if(line6bType == "Text"){input "line6bText", "text", required: true, title: "Text to show?"	}
		if(line6bType == "Device Attribute"){
		input "device17", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device17attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}	
		if(line6bType == "Image URL"){
		input "imageURL6b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth6b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight6b", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line6bType == "Device Icon"){
		input "monitor6bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor6b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line6cType", "enum", required: false, title: "Line6 Column C", submitOnChange: true, options: checkLineType()	 	
		if(line6cType == "Text"){input "line6cText", "text", required: true, title: "Text to show?"	}
		if(line6cType == "Device Attribute"){
		input "device18", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device18attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}	
		if(line6cType == "Image URL"){
		input "imageURL6c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth6c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight6c", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line6cType == "Device Icon"){
		input "monitor6cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor6c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line6dType", "enum", required: false, title: "Line6 Column D", submitOnChange: true, options: checkLineType()
		if(line6dType == "Text"){input "line6dText", "text", required: true, title: "Text to show?"	}
		if(line6dType == "Device Attribute"){
		input "device30", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device30attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line6dType == "Image URL"){
		input "imageURL6d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth6d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight6d", "number", title: "Enter Height", required: true, multiple: false
		
		}	
		if(line6dType == "Device Icon"){
		input "monitor6dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType() 	
		input "deviceMonitor6d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	 }
		   
  }  
}


def line7() {
       dynamicPage(name: "line7") {
		
	section ("Line 7 Configuration"){
			   
	input "line7aType", "enum", required: false, title: "Line7 Column A", submitOnChange: true, options: checkLineType()
		if(line7aType == "Text"){input "line7aText", "text", required: true, title: "Text to show?"	}
		if(line7aType == "Device Attribute"){
		input "device19", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device19attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line7aType == "Image URL"){
		input "imageURL7a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth7a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight7a", "number", title: "Enter Height", required: true, multiple: false	
		 
		}	
		if(line7aType == "Device Icon"){
		input "monitor7aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType() 	
		input "deviceMonitor7a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line7bType", "enum", required: false, title: "Line7 Column B", submitOnChange: true, options: checkLineType()
		if(line7bType == "Text"){input "line7bText", "text", required: true, title: "Text to show?"	}
		if(line7bType == "Device Attribute"){
		input "device20", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device20attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}	
		if(line7bType == "Image URL"){
		input "imageURL7b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth7b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight7b", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line7bType == "Device Icon"){
		input "monitor7bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor7b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line7cType", "enum", required: false, title: "Line7 Column C", submitOnChange: true, options: checkLineType()
		if(line7cType == "Text"){input "line7cText", "text", required: true, title: "Text to show?"	}
		if(line7cType == "Device Attribute"){
		input "device21", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device21attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}	
		if(line7cType == "Image URL"){
		input "imageURL7c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth7c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight7c", "number", title: "Enter Height", required: true, multiple: false	
			
		}
		if(line7cType == "Device Icon"){
		input "monitor7cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType() 	
		input "deviceMonitor7c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
		
	input "line7dType", "enum", required: false, title: "Line7 Column D", submitOnChange: true, options: checkLineType()
		if(line7dType == "Text"){input "line7dText", "text", required: true, title: "Text to show?"	}
		if(line7dType == "Device Attribute"){
		input "device31", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device31attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line7dType == "Image URL"){
		input "imageURL7d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth7d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight7d", "number", title: "Enter Height", required: true, multiple: false
		
		}	
		if(line7dType == "Device Icon"){
		input "monitor7dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor7d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	 }	
		  
  }  
}


def line8() {
       dynamicPage(name: "line8") {
		
	section ("Line 8 Configuration"){
			   
	input "line8aType", "enum", required: false, title: "Line8 Column A", submitOnChange: true, options: checkLineType() 		
		if(line8aType == "Text"){input "line8aText", "text", required: true, title: "Text to show?"	}
		if(line8aType == "Device Attribute"){
		input "device22", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device22attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line8aType == "Image URL"){
		input "imageURL8a", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth8a", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight8a", "number", title: "Enter Height", required: true, multiple: false	
		
		}			
		if(line8aType == "Device Icon"){
		input "monitor8aType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor8a", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
	
	input "line8bType", "enum", required: false, title: "Line8 Column B", submitOnChange: true, options: checkLineType()	
		if(line8bType == "Text"){input "line8bText", "text", required: true, title: "Text to show?"	}
		if(line8bType == "Device Attribute"){
		input "device23", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device23attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line8bType == "Image URL"){
		input "imageURL8b", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth8b", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight8b", "number", title: "Enter Height", required: true, multiple: false	
		
		}	
		if(line8bType == "Device Icon"){
		input "monitor8bType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor8b", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line8cType", "enum", required: false, title: "Line8 Column C", submitOnChange: true, options: checkLineType()
		if(line8cType == "Text"){input "line8cText", "text", required: true, title: "Text to show?"	}
		if(line8cType == "Device Attribute"){
		input "device24", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device24attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line8cType == "Image URL"){
		input "imageURL8c", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth8c", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight8c", "number", title: "Enter Height", required: true, multiple: false	
		
		}
		if(line8cType == "Device Icon"){
		input "monitor8cType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor8c", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}
		
	input "line8dType", "enum", required: false, title: "Line8 Column D", submitOnChange: true, options: checkLineType() 	
		if(line8dType == "Text"){input "line8dText", "text", required: true, title: "Text to show?"	}
		if(line8dType == "Device Attribute"){
		input "device32", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		input "device32attrib", "text", title: "Enter Device Attribute", required: true, multiple: false	
		}
		if(line8dType == "Image URL"){
		input "imageURL8d", "text", title: "Image URL", required: true, multiple: false, submitOnChange: true	
		input "imageWidth8d", "number", title: "Enter Width", required: true, multiple: false	
		input "imageHeight8d", "number", title: "Enter Height", required: true, multiple: false
		
		}
		if(line8dType == "Device Icon"){
		input "monitor8dType", "enum", required: true, title: "Device Type", submitOnChange: true, options: checkMonitorType()	
		input "deviceMonitor8d", "capability.*", title: "Select Device", required: true, multiple: false, submitOnChange: true	
		}	
	 }
		   
  }  
}
def device1aHandler(evt){
	LOGDEBUG( "device1aHandler - evt = $evt.value")
	state.dev1a = evt.value
	if(state.dev1a == "open"){state.icon1a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1a == "closed"){state.icon1a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1a == "on" && monitor1aType == "Switch"){state.icon1a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1a == "off" && monitor1aType == "Switch"){state.icon1a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1a == "on" && monitor1aType == "Light"){state.icon1a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1a == "off" && monitor1aType == "Light"){state.icon1a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1a == "wet"){state.icon1a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1a == "dry"){state.icon1a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1a == "present"){state.icon1a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1a == "not present"){state.icon1a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1a == "locked"){state.icon1a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev1a == "unlocked"){state.icon1a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon1a = $state.icon1a")
	sendLines()
}
def device1bHandler(evt){
	LOGDEBUG( "device1bHandler - evt = $evt.value")
	state.dev1b = evt.value
	if(state.dev1b == "open"){state.icon1b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1b == "closed"){state.icon1b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1b == "on" && monitor1bType == "Switch"){state.icon1b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1b == "off" && monitor1bType == "Switch"){state.icon1b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1b == "on" && monitor1bType == "Light"){state.icon1b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1b == "off" && monitor1bType == "Light"){state.icon1b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1b == "wet"){state.icon1b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1b == "dry"){state.icon1b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1b == "present"){state.icon1b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1b == "not present"){state.icon1b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1b == "locked"){state.icon1b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev1b == "unlocked"){state.icon1b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon1b = $state.icon1b")
	sendLines()
}
def device1cHandler(evt){
	LOGDEBUG( "device1cHandler - evt = $evt.value")
	state.dev1c = evt.value
	if(state.dev1c == "open"){state.icon1c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1c == "closed"){state.icon1c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1c == "on" && monitor1cType == "Switch"){state.icon1c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1c == "off" && monitor1cType == "Switch"){state.icon1c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1c == "on" && monitor1cType == "Light"){state.icon1c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1c == "off" && monitor1cType == "Light"){state.icon1c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1c == "wet"){state.icon1c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1c == "dry"){state.icon1c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1c == "present"){state.icon1c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1c == "not present"){state.icon1c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1c == "locked"){state.icon1c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev1c == "unlocked"){state.icon1c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon1c = $state.icon1c")
	sendLines()
}
def device1dHandler(evt){
	LOGDEBUG( "device1dHandler - evt = $evt.value")
	state.dev1d = evt.value
	if(state.dev1d == "open"){state.icon1d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1d == "closed"){state.icon1d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev1d == "on" && monitor1dType == "Switch"){state.icon1d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1d == "off" && monitor1dType == "Switch"){state.icon1d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev1d == "on" && monitor1dType == "Light"){state.icon1d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1d == "off" && monitor1dType == "Light"){state.icon1d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev1d == "wet"){state.icon1d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1d == "dry"){state.icon1d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev1d == "present"){state.icon1d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1d == "not present"){state.icon1d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev1d == "locked"){state.icon1d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev1d == "unlocked"){state.icon1d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon1d = $state.icon1d")
	sendLines()
}
def device2aHandler(evt){
	LOGDEBUG( "device2aHandler - evt = $evt.value")
	state.dev2a = evt.value
	if(state.dev2a == "open"){state.icon2a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2a == "closed"){state.icon2a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2a == "on" && monitor2aType == "Switch"){state.icon2a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2a == "off" && monitor2aType == "Switch"){state.icon2a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2a == "on" && monitor2aType == "Light"){state.icon2a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2a == "off" && monitor2aType == "Light"){state.icon2a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2a == "wet"){state.icon2a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2a == "dry"){state.icon2a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2a == "present"){state.icon2a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2a == "not present"){state.icon2a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2a == "locked"){state.icon2a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev2a == "unlocked"){state.icon2a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon2a = $state.icon2a")
	sendLines()
}
def device2bHandler(evt){
	LOGDEBUG( "device2bHandler - evt = $evt.value")
	state.dev2b = evt.value
	if(state.dev2b == "open"){state.icon2b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2b == "closed"){state.icon2b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2b == "on" && monitor2bType == "Switch"){state.icon2b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2b == "off" && monitor2bType == "Switch"){state.icon2b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2b == "on" && monitor2bType == "Light"){state.icon2b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2b == "off" && monitor2bType == "Light"){state.icon2b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2b == "wet"){state.icon2b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2b == "dry"){state.icon2b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2b == "present"){state.icon2b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2b == "not present"){state.icon2b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2b == "locked"){state.icon2b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev2b == "unlocked"){state.icon2b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon2b = $state.icon2b")
	sendLines()
}
def device2cHandler(evt){
	LOGDEBUG( "device2cHandler - evt = $evt.value")
	state.dev2c = evt.value
	if(state.dev2c == "open"){state.icon2c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2c == "closed"){state.icon2c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2c == "on" && monitor2cType == "Switch"){state.icon2c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2c == "off" && monitor2cType == "Switch"){state.icon2c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2c == "on" && monitor2cType == "Light"){state.icon2c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2c == "off" && monitor2cType == "Light"){state.icon2c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2c == "wet"){state.icon2c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2c == "dry"){state.icon2c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2c == "present"){state.icon2c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2c == "not present"){state.icon2c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2c == "locked"){state.icon2c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev2c == "unlocked"){state.icon2c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon2c = $state.icon2c")
	sendLines()
}
def device2dHandler(evt){
	LOGDEBUG( "device2dHandler - evt = $evt.value")
	state.dev2d = evt.value
	if(state.dev2d == "open"){state.icon2d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2d == "closed"){state.icon2d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev2d == "on" && monitor2dType == "Switch"){state.icon2d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2d == "off" && monitor2dType == "Switch"){state.icon2d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev2d == "on" && monitor2dType == "Light"){state.icon2d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2d == "off" && monitor2dType == "Light"){state.icon2d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev2d == "wet"){state.icon2d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2d == "dry"){state.icon2d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev2d == "present"){state.icon2d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2d == "not present"){state.icon2d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev2d == "locked"){state.icon2d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev2d == "unlocked"){state.icon2d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon2d = $state.icon2d")
	sendLines()
}

def device3aHandler(evt){
	LOGDEBUG( "device3aHandler - evt = $evt.value")
	state.dev3a = evt.value
	if(state.dev3a == "open"){state.icon3a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3a == "closed"){state.icon3a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3a == "on" && monitor3aType == "Switch"){state.icon3a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3a == "off" && monitor3aType == "Switch"){state.icon3a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3a == "on" && monitor3aType == "Light"){state.icon3a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3a == "off" && monitor3aType == "Light"){state.icon3a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3a == "wet"){state.icon3a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3a == "dry"){state.icon3a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3a == "present"){state.icon3a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3a == "not present"){state.icon3a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3a == "locked"){state.icon3a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev3a == "unlocked"){state.icon3a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon3a = $state.icon3a")
	sendLines()
}
def device3bHandler(evt){
	LOGDEBUG( "device3bHandler - evt = $evt.value")
	state.dev3b = evt.value
	if(state.dev3b == "open"){state.icon3b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3b == "closed"){state.icon3b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3b == "on" && monitor3bType == "Switch"){state.icon3b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3b == "off" && monitor3bType == "Switch"){state.icon3b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3b == "on" && monitor3bType == "Light"){state.icon3b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3b == "off" && monitor3bType == "Light"){state.icon3b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3b == "wet"){state.icon3b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3b == "dry"){state.icon3b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3b == "present"){state.icon3b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3b == "not present"){state.icon3b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3b == "locked"){state.icon3b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev3b == "unlocked"){state.icon3b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon3b = $state.icon3b")
	sendLines()
}
def device3cHandler(evt){
	LOGDEBUG( "device3cHandler - evt = $evt.value")
	state.dev3c = evt.value
	if(state.dev3c == "open"){state.icon3c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3c == "closed"){state.icon3c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3c == "on" && monitor3cType == "Switch"){state.icon3c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3c == "off" && monitor3cType == "Switch"){state.icon3c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3c == "on" && monitor3cType == "Light"){state.icon3c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3c == "off" && monitor3cType == "Light"){state.icon3c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3c == "wet"){state.icon3c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3c == "dry"){state.icon3c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3c == "present"){state.icon3c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3c == "not present"){state.icon3c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3c == "locked"){state.icon3c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev3c == "unlocked"){state.icon3c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon3c = $state.icon3c")
	sendLines()
}
def device3dHandler(evt){
	LOGDEBUG( "device3dHandler - evt = $evt.value")
	state.dev3d = evt.value
	if(state.dev3d == "open"){state.icon3d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3d == "closed"){state.icon3d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev3d == "on" && monitor3dType == "Switch"){state.icon3d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3d == "off" && monitor3dType == "Switch"){state.icon3d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev3d == "on" && monitor3dType == "Light"){state.icon3d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3d == "off" && monitor3dType == "Light"){state.icon3d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev3d == "wet"){state.icon3d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3d == "dry"){state.icon3d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev3d == "present"){state.icon3d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3d == "not present"){state.icon3d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev3d == "locked"){state.icon3d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev3d == "unlocked"){state.icon3d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon3d = $state.icon3d")
	sendLines()
}

def device4aHandler(evt){
	LOGDEBUG( "device4aHandler - evt = $evt.value")
	state.dev4a = evt.value
	if(state.dev4a == "open"){state.icon4a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4a == "closed"){state.icon4a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4a == "on" && monitor4aType == "Switch"){state.icon4a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4a == "off" && monitor4aType == "Switch"){state.icon4a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4a == "on" && monitor4aType == "Light"){state.icon4a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4a == "off" && monitor4aType == "Light"){state.icon4a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4a == "wet"){state.icon4a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4a == "dry"){state.icon4a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4a == "present"){state.icon4a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4a == "not present"){state.icon4a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4a == "locked"){state.icon4a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev4a == "unlocked"){state.icon4a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon4a = $state.icon4a")
	sendLines()
}
def device4bHandler(evt){
	LOGDEBUG( "device4bHandler - evt = $evt.value")
	state.dev4b = evt.value
	if(state.dev4b == "open"){state.icon4b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4b == "closed"){state.icon4b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4b == "on" && monitor4bType == "Switch"){state.icon4b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4b == "off" && monitor4bType == "Switch"){state.icon4b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4b == "on" && monitor4bType == "Light"){state.icon4b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4b == "off" && monitor4bType == "Light"){state.icon4b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4b == "wet"){state.icon4b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4b == "dry"){state.icon4b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4b == "present"){state.icon4b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4b == "not present"){state.icon4b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4b == "locked"){state.icon4b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev4b == "unlocked"){state.icon4b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon4b = $state.icon4b")
	sendLines()
}
def device4cHandler(evt){
	LOGDEBUG( "device4cHandler - evt = $evt.value")
	state.dev4c = evt.value
	if(state.dev4c == "open"){state.icon4c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4c == "closed"){state.icon4c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4c == "on" && monitor4cType == "Switch"){state.icon4c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4c == "off" && monitor4cType == "Switch"){state.icon4c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4c == "on" && monitor4cType == "Light"){state.icon4c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4c == "off" && monitor4cType == "Light"){state.icon4c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4c == "wet"){state.icon4c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4c == "dry"){state.icon4c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4c == "present"){state.icon4c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4c == "not present"){state.icon4c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4c == "locked"){state.icon4c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev4c == "unlocked"){state.icon4c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon4c = $state.icon4c")
	sendLines()
}
def device4dHandler(evt){
	LOGDEBUG( "device4dHandler - evt = $evt.value")
	state.dev4d = evt.value
	if(state.dev4d == "open"){state.icon4d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4d == "closed"){state.icon4d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev4d == "on" && monitor4dType == "Switch"){state.icon4d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4d == "off" && monitor4dType == "Switch"){state.icon4d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev4d == "on" && monitor4dType == "Light"){state.icon4d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4d == "off" && monitor4dType == "Light"){state.icon4d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev4d == "wet"){state.icon4d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4d == "dry"){state.icon4d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev4d == "present"){state.icon4d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4d == "not present"){state.icon4d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev4d == "locked"){state.icon4d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev4d == "unlocked"){state.icon4d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon4d = $state.icon4d")
	sendLines()
}

def device5aHandler(evt){
	LOGDEBUG( "device5aHandler - evt = $evt.value")
	state.dev5a = evt.value
	if(state.dev5a == "open"){state.icon5a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5a == "closed"){state.icon5a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5a == "on" && monitor5aType == "Switch"){state.icon5a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5a == "off" && monitor5aType == "Switch"){state.icon5a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5a == "on" && monitor5aType == "Light"){state.icon5a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5a == "off" && monitor5aType == "Light"){state.icon5a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5a == "wet"){state.icon5a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5a == "dry"){state.icon5a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5a == "present"){state.icon5a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5a == "not present"){state.icon5a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5a == "locked"){state.icon5a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev5a == "unlocked"){state.icon5a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon5a = $state.icon5a")
	sendLines()
}
def device5bHandler(evt){
	LOGDEBUG( "device5bHandler - evt = $evt.value")
	state.dev5b = evt.value
	if(state.dev5b == "open"){state.icon5b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5b == "closed"){state.icon5b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5b == "on" && monitor5bType == "Switch"){state.icon5b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5b == "off" && monitor5bType == "Switch"){state.icon5b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5b == "on" && monitor5bType == "Light"){state.icon5b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5b == "off" && monitor5bType == "Light"){state.icon5b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5b == "wet"){state.icon5b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5b == "dry"){state.icon5b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5b == "present"){state.icon5b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5b == "not present"){state.icon5b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5b == "locked"){state.icon5b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev5b == "unlocked"){state.icon5b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon5b = $state.icon5b")
	sendLines()
}
def device5cHandler(evt){
	LOGDEBUG( "device5cHandler - evt = $evt.value")
	state.dev5c = evt.value
	if(state.dev5c == "open"){state.icon5c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5c == "closed"){state.icon5c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5c == "on" && monitor5cType == "Switch"){state.icon5c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5c == "off" && monitor5cType == "Switch"){state.icon5c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5c == "on" && monitor5cType == "Light"){state.icon5c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5c == "off" && monitor5cType == "Light"){state.icon5c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5c == "wet"){state.icon5c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5c == "dry"){state.icon5c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5c == "present"){state.icon5c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5c == "not present"){state.icon5c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5c == "locked"){state.icon5c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev5c == "unlocked"){state.icon5c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon5c = $state.icon5c")
	sendLines()
}
def device5dHandler(evt){
	LOGDEBUG( "device5dHandler - evt = $evt.value")
	state.dev5d = evt.value
	if(state.dev5d == "open"){state.icon5d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5d == "closed"){state.icon5d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev5d == "on" && monitor5dType == "Switch"){state.icon5d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5d == "off" && monitor5dType == "Switch"){state.icon5d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev5d == "on" && monitor5dType == "Light"){state.icon5d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5d == "off" && monitor5dType == "Light"){state.icon5d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev5d == "wet"){state.icon5d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5d == "dry"){state.icon5d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev5d == "present"){state.icon5d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5d == "not present"){state.icon5d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev5d == "locked"){state.icon5d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev5d == "unlocked"){state.icon5d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon5d = $state.icon5d")
	sendLines()
}

def device6aHandler(evt){
	LOGDEBUG( "device6aHandler - evt = $evt.value")
	state.dev6a = evt.value
	if(state.dev6a == "open"){state.icon6a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6a == "closed"){state.icon6a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6a == "on" && monitor6aType == "Switch"){state.icon6a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6a == "off" && monitor6aType == "Switch"){state.icon6a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6a == "on" && monitor6aType == "Light"){state.icon6a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6a == "off" && monitor6aType == "Light"){state.icon6a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6a == "wet"){state.icon6a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6a == "dry"){state.icon6a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6a == "present"){state.icon6a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6a == "not present"){state.icon6a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6a == "locked"){state.icon6a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev6a == "unlocked"){state.icon6a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon6a = $state.icon6a")
	sendLines()
}
def device6bHandler(evt){
	LOGDEBUG( "device6bHandler - evt = $evt.value")
	state.dev6b = evt.value
	if(state.dev6b == "open"){state.icon6b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6b == "closed"){state.icon6b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6b == "on" && monitor6bType == "Switch"){state.icon6b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6b == "off" && monitor6bType == "Switch"){state.icon6b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6b == "on" && monitor6bType == "Light"){state.icon6b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6b == "off" && monitor6bType == "Light"){state.icon6b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6b == "wet"){state.icon6b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6b == "dry"){state.icon6b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6b == "present"){state.icon6b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6b == "not present"){state.icon6b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6b == "locked"){state.icon6b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev6b == "unlocked"){state.icon6b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon6b = $state.icon6b")
	sendLines()
}
def device6cHandler(evt){
	LOGDEBUG( "device6cHandler - evt = $evt.value")
	state.dev6c = evt.value
	if(state.dev6c == "open"){state.icon6c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6c == "closed"){state.icon6c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6c == "on" && monitor6cType == "Switch"){state.icon6c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6c == "off" && monitor6cType == "Switch"){state.icon6c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6c == "on" && monitor6cType == "Light"){state.icon6c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6c == "off" && monitor6cType == "Light"){state.icon6c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6c == "wet"){state.icon6c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6c == "dry"){state.icon6c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6c == "present"){state.icon6c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6c == "not present"){state.icon6c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6c == "locked"){state.icon6c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev6c == "unlocked"){state.icon6c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon6c = $state.icon6c")
	sendLines()
}
def device6dHandler(evt){
	LOGDEBUG( "device6dHandler - evt = $evt.value")
	state.dev6d = evt.value
	if(state.dev6d == "open"){state.icon6d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6d == "closed"){state.icon6d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev6d == "on" && monitor6dType == "Switch"){state.icon6d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6d == "off" && monitor6dType == "Switch"){state.icon6d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev6d == "on" && monitor6dType == "Light"){state.icon6d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6d == "off" && monitor6dType == "Light"){state.icon6d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev6d == "wet"){state.icon6d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6d == "dry"){state.icon6d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev6d == "present"){state.icon6d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6d == "not present"){state.icon6d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev6d == "locked"){state.icon6d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev6d == "unlocked"){state.icon6d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon6d = $state.icon6d")
	sendLines()
}

def device7aHandler(evt){
	LOGDEBUG( "device7aHandler - evt = $evt.value")
	state.dev7a = evt.value
	if(state.dev7a == "open"){state.icon7a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7a == "closed"){state.icon7a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7a == "on" && monitor7aType == "Switch"){state.icon7a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7a == "off" && monitor7aType == "Switch"){state.icon7a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7a == "on" && monitor7aType == "Light"){state.icon7a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7a == "off" && monitor7aType == "Light"){state.icon7a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7a == "wet"){state.icon7a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7a == "dry"){state.icon7a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7a == "present"){state.icon7a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7a == "not present"){state.icon7a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7a == "locked"){state.icon7a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev7a == "unlocked"){state.icon7a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon7a = $state.icon7a")
	sendLines()
}
def device7bHandler(evt){
	LOGDEBUG( "device7bHandler - evt = $evt.value")
	state.dev7b = evt.value
	if(state.dev7b == "open"){state.icon7b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7b == "closed"){state.icon7b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7b == "on" && monitor7bType == "Switch"){state.icon7b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7b == "off" && monitor7bType == "Switch"){state.icon7b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7b == "on" && monitor7bType == "Light"){state.icon7b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7b == "off" && monitor7bType == "Light"){state.icon7b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7b == "wet"){state.icon7b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7b == "dry"){state.icon7b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7b == "present"){state.icon7b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7b == "not present"){state.icon7b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7b == "locked"){state.icon7b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev7b == "unlocked"){state.icon7b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon7b = $state.icon7b")
	sendLines()
}
def device7cHandler(evt){
	LOGDEBUG( "device7cHandler - evt = $evt.value")
	state.dev7c = evt.value
	if(state.dev7c == "open"){state.icon7c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7c == "closed"){state.icon7c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7c == "on" && monitor7cType == "Switch"){state.icon7c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7c == "off" && monitor7cType == "Switch"){state.icon7c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7c == "on" && monitor7cType == "Light"){state.icon7c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7c == "off" && monitor7cType == "Light"){state.icon7c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7c == "wet"){state.icon7c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7c == "dry"){state.icon7c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7c == "present"){state.icon7c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7c == "not present"){state.icon7c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7c == "locked"){state.icon7c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev7c == "unlocked"){state.icon7c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon7c = $state.icon7c")
	sendLines()
}
def device7dHandler(evt){
	LOGDEBUG( "device7dHandler - evt = $evt.value")
	state.dev7d = evt.value
	if(state.dev7d == "open"){state.icon7d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7d == "closed"){state.icon7d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev7d == "on" && monitor7dType == "Switch"){state.icon7d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7d == "off" && monitor7dType == "Switch"){state.icon7d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev7d == "on" && monitor7dType == "Light"){state.icon7d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7d == "off" && monitor7dType == "Light"){state.icon7d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev7d == "wet"){state.icon7d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7d == "dry"){state.icon7d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev7d == "present"){state.icon7d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7d == "not present"){state.icon7d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev7d == "locked"){state.icon7d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev7d == "unlocked"){state.icon7d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon7d = $state.icon7d")
	sendLines()
}

def device8aHandler(evt){
	LOGDEBUG( "device8aHandler - evt = $evt.value")
	state.dev8a = evt.value
	if(state.dev8a == "open"){state.icon8a = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8a == "closed"){state.icon8a = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8a == "on" && monitor8aType == "Switch"){state.icon8a = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8a == "off" && monitor8aType == "Switch"){state.icon8a = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8a == "on" && monitor8aType == "Light"){state.icon8a = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8a == "off" && monitor8aType == "Light"){state.icon8a = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8a == "wet"){state.icon8a = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8a == "dry"){state.icon8a = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8a == "present"){state.icon8a = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8a == "not present"){state.icon8a = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8a == "locked"){state.icon8a = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev8a == "unlocked"){state.icon8a = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon8a = $state.icon8a")
	sendLines()
}
def device8bHandler(evt){
	LOGDEBUG( "device8bHandler - evt = $evt.value")
	state.dev8b = evt.value
	if(state.dev8b == "open"){state.icon8b = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8b == "closed"){state.icon8b = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8b == "on" && monitor8bType == "Switch"){state.icon8b = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8b == "off" && monitor8bType == "Switch"){state.icon8b = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8b == "on" && monitor8bType == "Light"){state.icon8b = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8b == "off" && monitor8bType == "Light"){state.icon8b = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8b == "wet"){state.icon8b = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8b == "dry"){state.icon8b = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8b == "present"){state.icon8b = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8b == "not present"){state.icon8b = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8b == "locked"){state.icon8b = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev8b == "unlocked"){state.icon8b = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon8b = $state.icon8b")
	sendLines()
}
def device8cHandler(evt){
	LOGDEBUG( "device8cHandler - evt = $evt.value")
	state.dev8c = evt.value
	if(state.dev8c == "open"){state.icon8c = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8c == "closed"){state.icon8c = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8c == "on" && monitor8cType == "Switch"){state.icon8c = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8c == "off" && monitor8cType == "Switch"){state.icon8c = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8c == "on" && monitor8cType == "Light"){state.icon8c = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8c == "off" && monitor8cType == "Light"){state.icon8c = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8c == "wet"){state.icon8c = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8c == "dry"){state.icon8c = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8c == "present"){state.icon8c = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8c == "not present"){state.icon8c = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8c == "locked"){state.icon8c = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev8c == "unlocked"){state.icon8c = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon8c = $state.icon8c")
	sendLines()
}
def device8dHandler(evt){
	LOGDEBUG( "device8dHandler - evt = $evt.value")
	state.dev8d = evt.value
	if(state.dev8d == "open"){state.icon8d = "<img src='" +imageOpen +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8d == "closed"){state.icon8d = "<img src='" +imageClosed +"' width='" +imageContactWidth +"' height='" +imageContactHeight +"'>"}
	if(state.dev8d == "on" && monitor8dType == "Switch"){state.icon8d = "<img src='" +imageOn +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8d == "off" && monitor8dType == "Switch"){state.icon8d = "<img src='" +imageOff +"' width='" +imageSwitchWidth +"' height='" +imageSwitchHeight +"'>"}
	if(state.dev8d == "on" && monitor8dType == "Light"){state.icon8d = "<img src='" +imageLightOn +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8d == "off" && monitor8dType == "Light"){state.icon8d = "<img src='" +imageLightOff +"' width='" +imageLightWidth +"' height='" +imageLightHeight +"'>"}
	if(state.dev8d == "wet"){state.icon8d = "<img src='" +imageWet +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8d == "dry"){state.icon8d = "<img src='" +imageDry +"' width='" +imageWaterWidth +"' height='" +imageWaterHeight +"'>"}
	if(state.dev8d == "present"){state.icon8d = "<img src='" +imagePresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8d == "not present"){state.icon8d = "<img src='" +imageNotPresent +"' width='" +imagePresentWidth +"' height='" +imagePresentHeight +"'>"}
	if(state.dev8d == "locked"){state.icon8d = "<img src='" +imageLockLocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight +"'>"}
	if(state.dev8d == "unlocked"){state.icon8d = "<img src='" +imageLockUnlocked +"' width='" +imageLockWidth +"' height='" +imageLockHeight+"'>"}
	LOGDEBUG("state.icon8d = $state.icon8d")
	sendLines()
}
def sendLines(){
LOGDEBUG("Running sendlines... Updating data in each line & sending data to the dashboard")
state.imageURL1a = "<img src='" +imageURL1a +"' width='" +imageWidth1a +"' height='" +imageHeight1a +"'>"
state.imageURL1b = "<img src='" +imageURL1b +"' width='" +imageWidth1b +"' height='" +imageHeight1b +"'>"
state.imageURL1c = "<img src='" +imageURL1c +"' width='" +imageWidth1c +"' height='" +imageHeight1c +"'>"
state.imageURL1d = "<img src='" +imageURL1d +"' width='" +imageWidth1d +"' height='" +imageHeight1d +"'>"
state.imageURL2a = "<img src='" +imageURL2a +"' width='" +imageWidth2a +"' height='" +imageHeight2a +"'>"
state.imageURL2b = "<img src='" +imageURL2b +"' width='" +imageWidth2b +"' height='" +imageHeight2b +"'>"
state.imageURL2c = "<img src='" +imageURL2c +"' width='" +imageWidth2c +"' height='" +imageHeight2c +"'>"
state.imageURL2d = "<img src='" +imageURL2d +"' width='" +imageWidth2d +"' height='" +imageHeight2d +"'>"
state.imageURL3a = "<img src='" +imageURL3a +"' width='" +imageWidth3a +"' height='" +imageHeight3a +"'>"
state.imageURL3b = "<img src='" +imageURL3b +"' width='" +imageWidth3b +"' height='" +imageHeight3b +"'>"
state.imageURL3c = "<img src='" +imageURL3c +"' width='" +imageWidth3c +"' height='" +imageHeight3c +"'>"
state.imageURL3d = "<img src='" +imageURL3d +"' width='" +imageWidth3d +"' height='" +imageHeight3d +"'>"
state.imageURL4a = "<img src='" +imageURL4a +"' width='" +imageWidth4a +"' height='" +imageHeight4a +"'>"
state.imageURL4b = "<img src='" +imageURL4b +"' width='" +imageWidth4b +"' height='" +imageHeight4b +"'>"
state.imageURL4c = "<img src='" +imageURL4c +"' width='" +imageWidth4c +"' height='" +imageHeight4c +"'>"
state.imageURL4d = "<img src='" +imageURL4d +"' width='" +imageWidth4d +"' height='" +imageHeight4d +"'>"
state.imageURL5a = "<img src='" +imageURL5a +"' width='" +imageWidth5a +"' height='" +imageHeight5a +"'>"
state.imageURL5b = "<img src='" +imageURL5b +"' width='" +imageWidth5b +"' height='" +imageHeight5b +"'>"
state.imageURL5c = "<img src='" +imageURL5c +"' width='" +imageWidth5c +"' height='" +imageHeight5c +"'>"
state.imageURL5d = "<img src='" +imageURL5d +"' width='" +imageWidth5d +"' height='" +imageHeight5d +"'>"
state.imageURL6a = "<img src='" +imageURL6a +"' width='" +imageWidth6a +"' height='" +imageHeight6a +"'>"
state.imageURL6b = "<img src='" +imageURL6b +"' width='" +imageWidth6b +"' height='" +imageHeight6b +"'>"
state.imageURL6c = "<img src='" +imageURL6c +"' width='" +imageWidth6c +"' height='" +imageHeight6c +"'>"
state.imageURL6d = "<img src='" +imageURL6d +"' width='" +imageWidth6d +"' height='" +imageHeight6d +"'>"
state.imageURL7a = "<img src='" +imageURL7a +"' width='" +imageWidth7a +"' height='" +imageHeight7a +"'>"
state.imageURL7b = "<img src='" +imageURL7b +"' width='" +imageWidth7b +"' height='" +imageHeight7b +"'>"
state.imageURL7c = "<img src='" +imageURL7c +"' width='" +imageWidth7c +"' height='" +imageHeight7c +"'>"
state.imageURL7d = "<img src='" +imageURL7d +"' width='" +imageWidth7d +"' height='" +imageHeight7d +"'>"
state.imageURL8a = "<img src='" +imageURL8a +"' width='" +imageWidth8a +"' height='" +imageHeight8a +"'>"
state.imageURL8b = "<img src='" +imageURL8b +"' width='" +imageWidth8b +"' height='" +imageHeight8b +"'>"
state.imageURL8c = "<img src='" +imageURL8c +"' width='" +imageWidth8c +"' height='" +imageHeight8c +"'>"
state.imageURL8d = "<img src='" +imageURL8d +"' width='" +imageWidth8d +"' height='" +imageHeight8d +"'>"

	if(line1aType == "Blank" || line1aType == null){state.line1aVal = " "}
	if(line1aType == "Text"){state.line1aVal = line1aText}
	if(line1aType == "Device Attribute"){state.line1aVal = state.dev1Val}
	if(line1aType == "Image URL"){state.line1aVal = state.imageURL1a}
	if(line1aType == "Device Icon"){state.line1aVal = state.icon1a}
	
	if(line1bType == "Blank" || line1bType == null){state.line1bVal = " "}
	if(line1bType == "Text"){state.line1bVal = line1bText}
	if(line1bType == "Device Attribute"){state.line1bVal = state.dev2Val}
	if(line1bType == "Image URL"){state.line1bVal = state.imageURL1b}	
	if(line1bType == "Device Icon"){state.line1bVal = state.icon1b}
	
	if(line1cType == "Blank" || line1cType == null){state.line1cVal = " "}
	if(line1cType == "Text"){state.line1cVal = line1cText}
	if(line1cType == "Device Attribute"){state.line1cVal = state.dev3Val}
	if(line1cType == "Image URL"){state.line1cVal = state.imageURL1c}	
	if(line1cType == "Device Icon"){state.line1cVal = state.icon1c}
	
	if(line1dType == "Blank" || line1dType == null){state.line1dVal = " "}
	if(line1dType == "Text"){state.line1dVal = line1dText}
	if(line1dType == "Device Attribute"){state.line1dVal = state.dev25Val}
	if(line1dType == "Image URL"){state.line1dVal = state.imageURL1d}
	if(line1dType == "Device Icon"){state.line1dVal = state.icon1d}
	
	if(line2aType == "Blank" || line2aType == null){state.line2aVal = " "}
	if(line2aType == "Text"){state.line2aVal = line2aText}
	if(line2aType == "Device Attribute"){state.line2aVal = state.dev4Val}
	if(line2aType == "Image URL"){state.line2aVal = state.imageURL2a}	
	if(line2aType == "Device Icon"){state.line2aVal = state.icon2a}
	
	if(line2bType == "Blank" || line2bType == null){state.line2bVal = " "}
	if(line2bType == "Text"){state.line2bVal = line2bText}
	if(line2bType == "Device Attribute"){state.line2bVal = state.dev5Val}
	if(line2bType == "Image URL"){state.line2bVal = state.imageURL2b}	
	if(line2bType == "Device Icon"){state.line2bVal = state.icon2b}
	
	if(line2cType == "Blank" || line2cType == null){state.line2cVal = " "}
	if(line2cType == "Text"){state.line2cVal = line2cText}
	if(line2cType == "Device Attribute"){state.line2cVal = state.dev6Val}
	if(line2cType == "Image URL"){state.line2cVal = state.imageURL2c}	
	if(line2cType == "Device Icon"){state.line2cVal = state.icon2c}
	
	if(line2dType == "Blank" || line2dType == null){state.line2dVal = " "}
	if(line2dType == "Text"){state.line2dVal = line2dText}
	if(line2dType == "Device Attribute"){state.line2dVal = state.dev26Val}
	if(line2dType == "Image URL"){state.line2dVal = state.imageURL2d}
	if(line2dType == "Device Icon"){state.line2dVal = state.icon2d}
	
	if(line3aType == "Blank" || line3aType == null){state.line3aVal = " "}
	if(line3aType == "Text"){state.line3aVal = line3aText}
	if(line3aType == "Device Attribute"){state.line3aVal = state.dev7Val}
	if(line3aType == "Image URL"){state.line3aVal = state.imageURL3a}
	if(line3aType == "Device Icon"){state.line3aVal = state.icon3a}
	
	if(line3bType == "Blank" || line3bType == null){state.line3bVal = " "}
	if(line3bType == "Text"){state.line3bVal = line3bText}
	if(line3bType == "Device Attribute"){state.line3bVal = state.dev8Val}
	if(line3bType == "Image URL"){state.line3bVal = state.imageURL3b}
	if(line3bType == "Device Icon"){state.line3bVal = state.icon3b}
	
	if(line3cType == "Blank" || line3cType == null){state.line3cVal = " "}
	if(line3cType == "Text"){state.line3cVal = line3cText}
	if(line3cType == "Device Attribute"){state.line3cVal = state.dev9Val}
	if(line3cType == "Image URL"){state.line3cVal = state.imageURL3c}
	if(line3cType == "Device Icon"){state.line3cVal = state.icon3c}
	
	if(line3dType == "Blank" || line3dType == null){state.line3dVal = " "}
	if(line3dType == "Text"){state.line3dVal = line3dText}
	if(line3dType == "Device Attribute"){state.line3dVal = state.dev27Val}
	if(line3dType == "Image URL"){state.line3dVal = state.imageURL3d}
	if(line3dType == "Device Icon"){state.line3dVal = state.icon3d}
	
	if(line4aType == "Blank" || line4aType == null){state.line4aVal = " "}
	if(line4aType == "Text"){state.line4aVal = line4aText}
	if(line4aType == "Device Attribute"){state.line4aVal = state.dev10Val}
	if(line4aType == "Image URL"){state.line4aVal = state.imageURL4a}
	if(line4aType == "Device Icon"){state.line4aVal = state.icon4a}
	
	if(line4bType == "Blank" || line4bType == null){state.line4bVal = " "}
	if(line4bType == "Text"){state.line4bVal = line4bText}
	if(line4bType == "Device Attribute"){state.line4bVal = state.dev11Val}
	if(line4bType == "Image URL"){state.line4bVal = state.imageURL4b}
	if(line4bType == "Device Icon"){state.line4bVal = state.icon4b}
	
	if(line4cType == "Blank" || line4cType == null){state.line4cVal = " "}
	if(line4cType == "Text"){state.line4cVal = line4cText}
	if(line4cType == "Device Attribute"){state.line4cVal = state.dev12Val}
	if(line4cType == "Image URL"){state.line4cVal = state.imageURL4c}
	if(line4cType == "Device Icon"){state.line4cVal = state.icon4c}
	
	if(line4dType == "Blank" || line4dType == null){state.line4dVal = " "}
	if(line4dType == "Text"){state.line4dVal = line4dText}
	if(line4dType == "Device Attribute"){state.line4dVal = state.dev28Val}
	if(line4dType == "Image URL"){state.line4dVal = state.imageURL4d}
	if(line4dType == "Device Icon"){state.line4dVal = state.icon4d}
	
	if(line5aType == "Blank" || line5aType == null){state.line5aVal = " "}
	if(line5aType == "Text"){state.line5aVal = line5aText}
	if(line5aType == "Device Attribute"){state.line5aVal = state.dev13Val}
	if(line5aType == "Image URL"){state.line5aVal = state.imageURL5a}
	if(line5aType == "Device Icon"){state.line5aVal = state.icon5a}
	
	if(line5bType == "Blank" || line5bType == null){state.line5bVal = " "}
	if(line5bType == "Text"){state.line5bVal = line5bText}
	if(line5bType == "Device Attribute"){state.line5bVal = state.dev14Val}
	if(line5bType == "Image URL"){state.line5bVal = state.imageURL5b}
	if(line5bType == "Device Icon"){state.line5bVal = state.icon5b}
	
	if(line5cType == "Blank" || line5cType == null){state.line5cVal = " "}
	if(line5cType == "Text"){state.line5cVal = line5cText}
	if(line5cType == "Device Attribute"){state.line5cVal = state.dev15Val}
	if(line5cType == "Image URL"){state.line5cVal = state.imageURL5c}
	if(line5cType == "Device Icon"){state.line5cVal = state.icon5c}
	
	if(line5dType == "Blank" || line5dType == null){state.line5dVal = " "}
	if(line5dType == "Text"){state.line5dVal = line5dText}
	if(line5dType == "Device Attribute"){state.line5dVal = state.dev29Val}
	if(line5dType == "Image URL"){state.line5dVal = state.imageURL5d}
	if(line5dType == "Device Icon"){state.line5dVal = state.icon5d}
	
	if(line6aType == "Blank" || line6aType == null){state.line6aVal = " "}
	if(line6aType == "Text"){state.line6aVal = line6aText}
	if(line6aType == "Device Attribute"){state.line6aVal = state.dev16Val}
	if(line6aType == "Image URL"){state.line6aVal = state.imageURL6a}	
	if(line6aType == "Device Icon"){state.line6aVal = state.icon6a}
	
	if(line6bType == "Blank" || line6bType == null){state.line6bVal = " "}
	if(line6bType == "Text"){state.line6bVal = line6bText}
	if(line6bType == "Device Attribute"){state.line6bVal = state.dev17Val}
	if(line6bType == "Image URL"){state.line6bVal = state.imageURL6b}
	if(line6bType == "Device Icon"){state.line6bVal = state.icon6b}
	
	if(line6cType == "Blank" || line6cType == null){state.line6cVal = " "}
	if(line6cType == "Text"){state.line6cVal = line6cText}
	if(line6cType == "Device Attribute"){state.line6cVal = state.dev18Val}
	if(line6cType == "Image URL"){state.line6cVal = state.imageURL6c}	
	if(line6cType == "Device Icon"){state.line6cVal = state.icon6c}
	
	if(line6dType == "Blank" || line6dType == null){state.line6dVal = " "}
	if(line6dType == "Text"){state.line6dVal = line6dText}
	if(line6dType == "Device Attribute"){state.line6dVal = state.dev30Val}
	if(line6dType == "Image URL"){state.line6dVal = state.imageURL6d}
	if(line6dType == "Device Icon"){state.line6dVal = state.icon6d}
	
	if(line7aType == "Blank" || line7aType == null){state.line7aVal = " "}
	if(line7aType == "Text"){state.line7aVal = line7aText}
	if(line7aType == "Device Attribute"){state.line7aVal = state.dev19Val}
	if(line7aType == "Image URL"){state.line7aVal = state.imageURL7a}
	if(line7aType == "Device Icon"){state.line7aVal = state.icon7a}
	
	if(line7bType == "Blank" || line7bType == null){state.line7bVal = " "}
	if(line7bType == "Text"){state.line7bVal = line7bText}
	if(line7bType == "Device Attribute"){state.line7bVal = state.dev20Val}
	if(line7bType == "Image URL"){state.line7bVal = state.imageURL7b}
	if(line7bType == "Device Icon"){state.line7bVal = state.icon7b}
	
	if(line7cType == "Blank" || line7cType == null){state.line7cVal = " "}
	if(line7cType == "Text"){state.line7cVal = line7cText}
	if(line7cType == "Device Attribute"){state.line7cVal = state.dev21Val}
	if(line7cType == "Image URL"){state.line7cVal = state.imageURL7c}
	if(line7cType == "Device Icon"){state.line7cVal = state.icon7c}
	
	if(line7dType == "Blank" || line7dType == null){state.line7dVal = " "}
	if(line7dType == "Text"){state.line7dVal = line7dText}
	if(line7dType == "Device Attribute"){state.line7dVal = state.dev31Val}
	if(line7dType == "Image URL"){state.line7dVal = state.imageURL7d}
	if(line7dType == "Device Icon"){state.line7dVal = state.icon7d}
	
	if(line8aType == "Blank" || line8aType == null){state.line8aVal = " "}
	if(line8aType == "Text"){state.line8aVal = line8aText}
	if(line8aType == "Device Attribute"){state.line8aVal = state.dev22Val}
	if(line8aType == "Image URL"){state.line8aVal = state.imageURL8a}
	if(line8aType == "Device Icon"){state.line8aVal = state.icon8a}
	
	if(line8bType == "Blank" || line8bType == null){state.line8bVal = " "}
	if(line8bType == "Text"){state.line8bVal = line8bText}
	if(line8bType == "Device Attribute"){state.line8bVal = state.dev23Val}
	if(line8bType == "Image URL"){state.line8bVal = state.imageURL8b}
	if(line8bType == "Device Icon"){state.line8bVal = state.icon8b}
	
	if(line8cType == "Blank" || line8cType == null){state.line8cVal = " "}
	if(line8cType == "Text"){state.line8cVal = line8cText}
	if(line8cType == "Device Attribute"){state.line8cVal = state.dev24Val}
	if(line8cType == "Image URL"){state.line8cVal = state.imageURL8c}
	if(line8cType == "Device Icon"){state.line8cVal = state.icon8c}
	
	if(line8dType == "Blank" || line8dType == null){state.line8dVal = " "}
	if(line8dType == "Text"){state.line8dVal = line8dText}
	if(line8dType == "Device Attribute"){state.line8dVal = state.dev32Val}
	if(line8dType == "Image URL"){state.line8dVal = state.imageURL8d}
	if(line8dType == "Device Icon"){state.line8dVal = state.icon8d}

sendDash()
runIn(state.refreshInterval,sendDash)	
}
	

def setDone(){
	state.fz = fsize
if(state.fz == null){state.fontsDone = false}	
if(state.fz != null){state.fontsDone = true}	
if(imageOpen == null &&	imageOn == null && imageWet == null && imagePresent == null && imageLightOn == null && imageLockLocked == null){state.iconsDone = false}		
else {state.iconsDone = true}	
	
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
	def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
	checkButtons()
	
   
}


def logCheck(){
    state.checkLog = logLevel
	if(state.checkLog == "INFO"){log.info "Informational Logging Enabled"}
	if(state.checkLog == "DEBUG & INFO"){log.info "Debug & Info Logging Enabled"}
	if(state.checkLog == "NONE"){log.info "Further Logging Disabled"}
	if(logLevel == "DEBUG & INFO") runIn(1800,logsDown)
}

def logsDown(){
    log.warn "Debug logging disabled... Info logging enabled"
    app.updateSetting("logLevel",[value:"INFO",type:"enum"])
	if(logLevel == "INFO") runIn(86400,logsDownAgain) 
}

def logsDownAgain(){
    log.warn "Info logging disabled."
    app.updateSetting("logLevel",[value:"NONE",type:"enum"])
	
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
		else if(currentVer > newVer){
        	state.status = "You are using a BETA ($state.version) - Release Version: $newVerRaw"
        	log.warn "** <b>$state.status</b>) **"
        	state.UpdateInfo = "N/A"
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
		state.version = "2.3.1"	 
		state.InternalName = "SuperTileDisplay"
    	state.ExternalName = "Super Tile Display"
		state.preCheckMessage = "This app was designed to use a special Virtual Display device  to display data on a dashboard tile"
    	state.CobraAppCheck = "supertiledisplay.json"
		
}







	
