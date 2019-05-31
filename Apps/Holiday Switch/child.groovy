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
 *  Last Update: 09/05/2019
 *
 *  Changes:
 *
 *  V1.0.0 - POC
 *
 */
 
 
 
definition(
    name: "Holiday Switch Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Schedule a Holiday switch on a certain date/time",
    category: "Convenience",
        
    parent: "Cobra:Holiday Switch",
    
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: "",
    )


preferences {
	section() {
	page name: "mainPage", title: "", install: true, uninstall: true 
	}
}

def mainPage() {
	dynamicPage(name: "mainPage") {  
	preCheck()

	 section(){input "switch1",  "device.HolidaySwitch",  title: "Holiday Switch to Schedule", multiple: false, required: true}
		section("Holiday Start <hr>"){
		input "month1", "enum", title: "Select Start Month", required: true, options: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		input "date1", "enum", title: "Select Start Date", required: true, options: [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
		input "hour1", "enum", title: "Select Hour", required: true,  options: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
		input "min1", "number", title: "Enter Minute", required: true
		}
		section(""){}
		section("Holiday End <hr>"){	
		input "month2", "enum", title: "Select End Month", required: true, options: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		input "date2", "enum", title: "Select End Date", required: true, options: [ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
		input "hour2", "enum", title: "Select Hour", required: true,  options: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
		input "min2", "number", title: "Enter Minute", required: true
		}
		section(){
		input "holidayName", "text", title: "Name this holiday", required: true
         }
		section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
		section() {label title: "Enter a name for this automation", required: false}
     } 
}


           
def installed(){initialize()}
def updated(){initialize()}
def initialize(){
	version()
	subscribeNow()
	log.info "Initialised with settings: ${settings}"
	logCheck()	
}
def subscribeNow() {
	unsubscribe()
  // App Specific subscriptions & settings below here   
	
	subscribe(switch1, "switch", switchHandler1)
    calculateCron1()
    calculateCron2()    
   
   	
}




def calculateCron1(){

state.selectedMonth = month1    
    if(state.selectedMonth == "Jan"){state.runMonth = "1"}
    if(state.selectedMonth == "Feb"){state.runMonth = "2"}
    if(state.selectedMonth == "Mar"){state.runMonth = "3"}
    if(state.selectedMonth == "Apr"){state.runMonth = "4"}
    if(state.selectedMonth == "May"){state.runMonth = "5"}
    if(state.selectedMonth == "Jun"){state.runMonth = "6"}
    if(state.selectedMonth == "Jul"){state.runMonth = "7"}
    if(state.selectedMonth == "Aug"){state.runMonth = "8"}
    if(state.selectedMonth == "Sep"){state.runMonth = "9"}
    if(state.selectedMonth == "Oct"){state.runMonth = "10"}
    if(state.selectedMonth == "Nov"){state.runMonth = "11"}
    if(state.selectedMonth == "Dec"){state.runMonth = "12"}
 
state.selectedDate = date1
state.selectedHour = hour1
state.selectedMin = min1
state.schedule1 = "0 ${state.selectedMin} ${state.selectedHour} ${state.selectedDate} ${state.runMonth} ? *"
    
    log.info "state.schedule1 = $state.schedule1"
    schedule(state.schedule1, switchNow1) 


    
}


def calculateCron2(){

state.selectedMonth2 = month2    
    if(state.selectedMonth2 == "Jan"){state.runMonth2 = "1"}
    if(state.selectedMonth2 == "Feb"){state.runMonth2 = "2"}
    if(state.selectedMonth2 == "Mar"){state.runMonth2 = "3"}
    if(state.selectedMonth2 == "Apr"){state.runMonth2 = "4"}
    if(state.selectedMonth2 == "May"){state.runMonth2 = "5"}
    if(state.selectedMonth2 == "Jun"){state.runMonth2 = "6"}
    if(state.selectedMonth2 == "Jul"){state.runMonth2 = "7"}
    if(state.selectedMonth2 == "Aug"){state.runMonth2 = "8"}
    if(state.selectedMonth2 == "Sep"){state.runMonth2 = "9"}
    if(state.selectedMonth2 == "Oct"){state.runMonth2 = "10"}
    if(state.selectedMonth2 == "Nov"){state.runMonth2 = "11"}
    if(state.selectedMonth2 == "Dec"){state.runMonth2 = "12"}
 
state.selectedDate2 = date2
state.selectedHour2 = hour2
state.selectedMin2 = min2
state.schedule2 = "0 ${state.selectedMin2} ${state.selectedHour2} ${state.selectedDate2} ${state.runMonth2} ? *"
    
    log.info "state.schedule2 = $state.schedule2"
    schedule(state.schedule2, switchNow2) 


    
}



def switchNow1(){	
    switch1.OverrideOn(holidayName) 
}
def switchNow2(){
	 switch1.OverrideOff() 
}





def switchHandler1 (evt) {
def switching = evt.value
    if(switching == "on"){
        log.info "Switch is turned on"
    }
        
    if(switching == "off"){
        log.info "Switch is turned off"
    }    
}








def version(){
	setDefaults()
	pauseOrNot()
	logCheck()
	resetBtnName()
	def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck()) 
	checkButtons()
   
}






def logCheck(){
    state.checkLog = debugMode
    if(state.checkLog == true){log.info "All Logging Enabled"}
    if(state.checkLog == false){log.info "Further Logging Disabled"}
}

def LOGDEBUG(txt){
    try {
    	if (settings.debugMode) { log.debug("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data!")
    }
}
def LOGWARN(txt){
    try {
    	if (settings.debugMode) { log.warn("${app.label.replace(" ","_").toUpperCase()}  (App Version: ${state.version}) - ${txt}") }
    } catch(ex) {
    	log.error("LOGWARN unable to output requested data!")
    }
}



def display(){
    setDefaults()
    if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
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
    log.warn "Paused"}
    else{app.updateLabel(app.label + ("<font color = 'red'> (Paused) </font>" ))
    log.warn "App Paused - state.pauseApp = $state.pauseApp "   
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
	log.warn "An update is available - Telling the parent!"
	parent.childUpdate(true,state.updateMsg) 
}



def preCheck(){
	setVersion()
    state.appInstalled = app.getInstallationState()  
    if(state.appInstalled != 'COMPLETE'){
    section(){ paragraph "$state.preCheckMessage"}
    }
    if(state.appInstalled == 'COMPLETE'){
    display()   
 	}
}




def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
   	state.restrictRun = false
}



def setVersion(){
		state.version = "1.0.0"	 
		state.InternalName = "HolidaySwitchChild"
    	state.ExternalName = "Holiday Switch Child"
		state.preCheckMessage = "This app is designed to schedule a switch on during holiday periods"
		state.CobraAppCheck = "holidayswitch.json"
}




 
