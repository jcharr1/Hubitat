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
 *
 *  Last Update: 24/06/2019
 *
 *  Changes:
 *
 *  V1.3.0 - debug HSM trigger
 *  V1.2.0 - Added option for minutes countdown timer as well as seconds (Last minute will change to seconds)
 *  V1.1.0 - Added countdown to date formatting & Display license notification text on install
 *  V1.0.1 - debug
 *  V1.0.0 - POC
 *
 */

import groovy.time.*

definition(
	
    name:"Super Tile Countdown",
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
	page name: "start"
	page name: "stop"
	
}




def mainPage() {
dynamicPage(name: "mainPage") {   
preCheck()
	
	section() {input "countType", "enum", title: "Select Timer Type", required:true, defaultValue: "NONE", options: ["Countdown to a Date/Time", "Countdown Seconds", "Countdown Minutes"] , submitOnChange: true}
	
	if(countType == "Countdown Seconds"){
	
section (){input "vDevice", "device.SuperTileCountdownDisplay", title: "Virtual Countdown Display Device", required: true}	
section (){input "vDeviceCount", "number", title: "How many seconds to count down?", required: true}
	
section(){href "start", title:"Configure Start Trigger", description: ""}
section(){href "stop", title:"Configure Stop Trigger", description: ""}
section(){
input "over", "bool", title: "Superimpose this countdown over another Super Tile?", required: true, defaultValue: false, submitOnChange: true
if(over){input "vDevice2", "device.SuperTileDisplayDevice", title: "Superimpose over this Super Tile (When countdown active)", required: false}
}
	section (){
	input "switchIt", "bool", title: "Also control a switch with this countdown (Turns on for the duration of the count)", required: true, defaultValue: false, submitOnChange: true
		if(switchIt){input "switch10", "capability.switch", title: "Switch to control", required: true}}	
		
		section() {input "logLevel", "enum", title: "Set Logging Level", required:true, defaultValue: "NONE", options: ["NONE", "INFO", "DEBUG & INFO"]}
		section() {label title: "Enter a name for this automation", required: false}
	}
	

		if(countType == "Countdown Minutes"){
	
section (){input "vDevice", "device.SuperTileCountdownDisplay", title: "Virtual Countdown Display Device", required: true}	
section (){input "vDeviceCount", "number", title: "How many minutes to count down?", required: true}
	
section(){href "start", title:"Configure Start Trigger", description: ""}
section(){href "stop", title:"Configure Stop Trigger", description: ""}
section(){
input "over", "bool", title: "Superimpose this countdown over another Super Tile?", required: true, defaultValue: false, submitOnChange: true
if(over){input "vDevice2", "device.SuperTileDisplayDevice", title: "Superimpose over this Super Tile (When countdown active)", required: false}
}
	section (){
	input "switchIt", "bool", title: "Also control a switch with this countdown (Turns on for the duration of the count)", required: true, defaultValue: false, submitOnChange: true
		if(switchIt){input "switch10", "capability.switch", title: "Switch to control", required: true}}	
		
		section() {input "logLevel", "enum", title: "Set Logging Level", required:true, defaultValue: "NONE", options: ["NONE", "INFO", "DEBUG & INFO"]}
		section() {label title: "Enter a name for this automation", required: false}
	}
	

	
	if(countType == "Countdown to a Date/Time"){
		section (){input "vDevice", "device.SuperTileCountdownDisplay", title: "Virtual Countdown Display Device", required: true}	
	section() {	
		input "everyYear", "bool", title: "Every Year", required: true, defaultValue: false, submitOnChange: true
		if(!everyYear){	input "year1", "number", title: "Enter 4 digit year", required: true}
	
		input "month1", "enum", title: "Select Month", required: true, options: [ "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
		input "date1", "enum", title: "Select Day", required: true, options: [ "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"]
		input "hour1", "enum", title: "Select Hour", required: true,  options: [ "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]
		input "min1", "number", title: "Enter Minute", required: true
		input "displayText", "bool", title: "Display text at the end of the countdown", required: true, defaultValue: false, submitOnChange: true
		if(displayText){
		input "endText", "text", title: "Text to display when time reaches the end (If using 'TimeLeft' attribute)", required: true
		input "endTextDuration", "number", title: "How long to display", required: true
		input "displayMode", "bool", title: "Minutes (Off) - Hours(On)", required: true, defaultValue: false
		}
	}
		section("Output Format") {input "format1", "enum",  title: "TimeLeft Output Format", required:true, defaultValue: "Days", options: ["Days", "Weeks", "Years"]}
		section() {input "logLevel", "enum", title: "Set Logging Level", required:true, defaultValue: "NONE", options: ["NONE", "INFO", "DEBUG & INFO"]}
		section() {label title: "Enter a name for this automation", required: false}
		
	
		
	   }
}
	   }	
	  



def installed() {initialize()}
def updated() {initialize()}
def initialize() {
version()
log.info "Initialised with settings: ${settings}"
logCheck()
subscribeNow()}
def subscribeNow(){
unsubscribe()
if(countType == "Countdown Seconds" || countType == "Countdown Minutes"){ 
subscribe(location, "mode", modeEventHandler)
subscribe(location, "hsmStatus", hsmStatusHandler)
if(lock1){subscribe(lock1, "lock", lock1Handler)}
if(lock2){subscribe(lock2, "lock", lock2Handler)}
if(contact1){subscribe(contact1, "contact", contact1Handler)}
if(motion1){subscribe(motion1, "motion", motion1Handler)}
if(switch1){subscribe(switch1, "switch", switch1Handler)}
if(contact2){subscribe(contact2, "contact", contact2Handler)}
if(motion2){subscribe(motion2, "motion", motion2Handler)}
if(switch2){subscribe(switch2, "switch", switch2Handler)}
									}									 
subscribe(vDevice, "Countdown", countDownHandler)
subscribe(vDevice, "CountStatus", countDownStatusHandler)
if(modesYes == false){state.allow1 = true}
if(modesYes2 == false){state.allow2 = true}
state.stopNow = false
if(countType == "Countdown to a Date/Time"){countdownToEvent()}
	
	
}

def start() {
 dynamicPage(name: "start") {	   
section("<b>Timer Start</b> <hr>") {input "trigger1", "enum", title: "Select trigger to start timer", submitOnChange: true, required:true, options: ["Contact", "HSM", "Lock", "Motion", "Switch", "Mode"]}
if(trigger1 == "Motion"){
section() {
input(name:"motion1", type: "capability.motionSensor", title: "Select Motion Sensor(s) ", multiple: true, required: false)
input "motionMode1", "bool", title: "Start countdown motion 'Active' (on) or 'Inactive' (off)", required: true, defaultValue: false}}
if(trigger1 == "Contact"){
section() { 
input(name:"contact1", type: "capability.contactSensor", title: "Select Contact Sensor(s) ", multiple: true, required: false)
input "contactMode1", "bool", title: "Start countdown when contact 'Opens' (on) or 'Closes' (off)", required: true, defaultValue: false}}
if(trigger1 == "Switch"){
section() {
input(name:"switch1", type: "capability.switch", title: "Select Switch(es) ", multiple: true, required: false)
input "switchMode1", "bool", title: "Start countdown when switch turns 'On' or 'Off' ", required: true, defaultValue: false}}
if(trigger1 == "Mode"){section() {input "newMode1", "mode", title: "Start timer when Hub changes to this mode", multiple: true,  required: false}}
if(trigger1 == "HSM"){section() {input "startHSM", "enum", title: "Start timer when Hub changes to this HSM mode", multiple: true, submitOnChange: true, required: false,  options:  ["armingAway", "armingHome", "armingNight", "armedAway", "armedHome", "armedNight", "disarmed", "allDisarmed"]}} 
if(trigger1 == "Lock"){
section() { 
input(name:"lock1", type: "capability.lock", title: "Select Lock(s) ", multiple: true, required: false)
input "lockMode1", "bool", title: "Start countdown when lock is 'Locked' (on) or 'Unlocked' (off)", required: true, defaultValue: false
}}
section() {	
		input "modesYes", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
		if(modesYes){	
		input(name:"newModeA", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: true)
		}}
}}


def stop() {
dynamicPage(name: "stop") {		   
section("<b>Timer Stop</b> <hr>") {input "trigger2", "enum", title: "Select trigger to stop timer", submitOnChange: true, required:true, options: ["Contact", "HSM", "Lock", "Motion", "Switch", "Mode"]} 
if(trigger2 == "Motion"){
section() {
input(name:"motion2", type: "capability.motionSensor", title: "Select Motion Sensor(s) ", multiple: true, required: false)
input "motionMode2", "bool", title: "Stop countdown when motion 'Active' (on) or 'Inactive' (off)", required: true, defaultValue: false}}
if(trigger2 == "Contact"){
section() { 
input(name:"contact2", type: "capability.contactSensor", title: "Select Contact Sensor(s) ", multiple: true, required: false)
input "contactMode2", "bool", title: "Stop countdown when contact 'Opens' (on) or 'Closes' (off)", required: true, defaultValue: false}}	
if(trigger2 == "Switch"){
section() { 
input(name:"switch2", type: "capability.switch", title: "Select Switch(es) ", multiple: true, required: false)
input "switchMode2", "bool", title: "Stop countdown when switch turns 'On'  or 'Off' ", required: true, defaultValue: false}}
if(trigger2 == "Mode"){
section() {input "newMode2", "mode", title: "Stop timer when Hub changes to this mode", multiple: true,  required: false}}
if(trigger2 == "HSM"){section() { input "stopHSM", "enum", title: "Stop timer when Hub changes to this HSM mode", multiple: true, submitOnChange: true, required: false, options:  ["armingAway", "armingHome", "armingNight", "armedAway", "armedHome", "armedNight", "disarmed", "allDisarmed"]}} 
if(trigger2 == "Lock"){
section() { 
input(name:"lock2", type: "capability.lock", title: "Select Lock(s) ", multiple: true, required: false)
input "lockMode2", "bool", title: "Stop countdown when lock is 'Locked' (on) or 'Unlocked' (off)", required: true, defaultValue: false}}
section() {	
		input "modesYes2", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
		if(modesYes2){	
		input(name:"newModeB", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: true)
		}}
}}

	
def countdownToEvent(){
calculateEnd()
	state.endText = endText
	if(displayMode == true){state.endDuration = 3600* endTextDuration}
	if(displayMode == false){state.endDuration = 60* endTextDuration}
	 //3600* = hours  60* = Minutes
	if(state.stopNow == false){
def nowdate = new Date().format('dd.MM.yyyy HH:mm', location.timeZone)
LOGDEBUG("nowdate = $nowdate")	
Date start = Date.parse('dd.MM.yyyy HH:mm', nowdate)	
	Date end  = Date.parse('dd.MM.yyyy HH:mm', "${state.selectedDate}.${state.runMonth}.${state.selectedYear} ${state.selectedHour}:${state.selectedMin}")
	LOGINFO("Finish Date & Time: $end")
	def duration = TimeCategory.minus( end, start )
	
	if(format1 == "Days"){
	state.days = duration.days
	state.hours = duration.hours
	state.minutes = duration.minutes
	state.timeLeft = "<b>Days: </b>" +duration.days +" <b> Hrs: </b>" +duration.hours +" <b> Mins: </b>" +duration.minutes 	
	}
	
	if(format1 == "Weeks"){
	state.days = duration.days
	state.hours = duration.hours
	state.minutes = duration.minutes
	def	days = state.days
	int weeks = (days % 365) / 7
		days  = (days % 365) % 7
	state.timeLeft = "<b>Weeks: </b>" +weeks +" <b> Days: </b>" +days +" <b> Hrs: </b>" +duration.hours +" <b> Mins: </b>" +duration.minutes 
		
	}
	
	if(format1 == "Years"){
	state.days = duration.days
	state.hours = duration.hours
	state.minutes = duration.minutes
	def	days = state.days
		int years = (days / 365)
		int months = (days % 365) % 30
        int weeks = (days % 365) / 7
            days  = (days % 365) % 7	
	
	state.timeLeft = "<b>Years: </b>" +years +" <b> Weeks: </b>" +weeks +" <b> Days: </b>" +days +" <b> Hrs: </b>" +duration.hours +" <b> Mins: </b>" +duration.minutes 
		
//		log.warn "result = years: $years  - weeks: $weeks - days: $days - hrs = $state.hours - mins: $state.minutes"
	}
	
	

	
LOGDEBUG("Time left = $state.timeLeft")	
	if(duration.days > 0 || duration.hours > 0 || duration.minutes > 0){

	vDevice.daysLeft(state.days)
	vDevice.hoursLeft(state.hours)
	vDevice.minutesLeft(state.minutes)
	vDevice.timeLeft(state.timeLeft)
	
	}
	if(duration.days == 0 && duration.hours == 0 && duration.minutes == 0){
	 if(everyYear == false){
		 state.stopNow = true
		 LOGINFO("Countdown Complete!")
		 if(state.endText){vDevice.timeLeft(state.endText)}
		 else {vDevice.timeLeft("Countdown Complete!")}
		vDevice.refresh()
		
	 }
		
	if(everyYear == true){
	 state.stopNow = false
		if(state.endText){vDevice.timeLeft(state.endText)}
		 else {vDevice.timeLeft("Countdown Complete! Recalculating for next year")}
	vDevice.refresh()
	
	if(displayMode == true){LOGDEBUG("Waiting $state.endDuration hrs before restarting (ready for next year)")}	
	if(displayMode == true){LOGDEBUG("Waiting $state.endDuration minutes before restarting (ready for next year)")}	
	runIn(state.endDuration, countdownToEvent)	
	}
	}
	else{runIn(60, countdownToEvent)}
	}}

def calculateEnd(){

state.selectedMonth = month1    
    if(state.selectedMonth == "Jan"){state.runMonth = "01"}
    if(state.selectedMonth == "Feb"){state.runMonth = "02"}
    if(state.selectedMonth == "Mar"){state.runMonth = "03"}
    if(state.selectedMonth == "Apr"){state.runMonth = "04"}
    if(state.selectedMonth == "May"){state.runMonth = "05"}
    if(state.selectedMonth == "Jun"){state.runMonth = "06"}
    if(state.selectedMonth == "Jul"){state.runMonth = "07"}
    if(state.selectedMonth == "Aug"){state.runMonth = "08"}
    if(state.selectedMonth == "Sep"){state.runMonth = "09"}
    if(state.selectedMonth == "Oct"){state.runMonth = "10"}
    if(state.selectedMonth == "Nov"){state.runMonth = "11"}
    if(state.selectedMonth == "Dec"){state.runMonth = "12"}
	
	
def nowYear = new Date().format('yyyy', location.timeZone)
	state.year = nowYear.toInteger()
	LOGDEBUG("This Year = $state.year")
	if(everyYear == true){
	def nowMonth = new Date().format('MM', location.timeZone)
	state.monthNow = nowMonth.toInteger()	
	LOGDEBUG("This Month = $state.monthNow")
if(state.timeLeft != null && state.timeLeft.contains("-"))	{
	LOGDEBUG("Need to add a year to correct calculations...")	
	state.selectedYear = state.year +1
	LOGDEBUG("Calculated year = $state.selectedYear")
		}
		else{state.selectedYear = state.year}	
	}	
if(everyYear == false){
	if(state.timeLeft != null && state.timeLeft.contains("-"))	{log.error "***************  WARNING!! ************** You have set a date or time in the past!!"}
	state.selectedYear = year1
}
state.selectedDate = date1
state.selectedHour = hour1
state.selectedMin = min1 //.toInteger()

}


	

def countDownHandler(evt){
state.finalSend = evt.value
if(state.finalSend != 0){
if(vDevice2 && over){vDevice2.overRide(state.finalSend)}
LOGDEBUG("Countdown Value = $state.finalSend")}}
def countDownStatusHandler(evt){
state.countdownStatus = evt.value
LOGDEBUG("Countdown Status $state.countdownStatus")
if(vDevice2 && over){
if(state.countdownStatus == "Stopped"){
pause 1000
	if(switch10){switch10.off()}
vDevice2.overRide("Stop")
	pause 1000 
vDevice2.refresh
}}}

def modeEventHandler(evt){
	 
	state.modeNow = evt.value    
	state.modeRequired = newModeA
	state.modeRequired2 = newModeB
	state.triggerMode1 = newMode1
	state.triggerMode2 = newMode2
	if(state.modeRequired != null){
	LOGDEBUG("state.modeRequired = $state.modeRequired") 
	LOGDEBUG("Mode Required = $state.modeRequired - current mode = $state.modeNow")  
	if(state.modeRequired.contains(location.mode) || state.modeRequired == null){ 
	LOGDEBUG("Mode is now $state.modeRequired")   
	state.allow1 = true
	}
	else{  
	state.allow1 = false
	LOGDEBUG("Mode not matched")
	}
	}
	 else {LOGDEBUG("Start mode restriction not used")}
	
	
	if(state.modeRequired2 != null){
		LOGDEBUG("state.modeRequired2 = $state.modeRequired2") 
		LOGDEBUG("Mode Required2 = $state.modeRequired2 - current mode = $state.modeNow")
		if(state.modeRequired2.contains(location.mode) ){ 
	LOGDEBUG("Mode is now $state.modeRequired2")   
	state.allow2 = true
	}
	else{  
	state.allow2 = false
	LOGDEBUG("Mode not matched")
	}
	}
	else {LOGDEBUG("Stop mode restriction not used")}
	
	
	
		if(state.allow1 == true){
LOGDEBUG("Mode change handler running")
state.modeNow = evt.value    
	if(state.triggerMode1 != null){
if(state.triggerMode1.contains(location.mode)){ 
LOGDEBUG("Mode is now $state.modeNow") 
    
    if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
    
}}}
	if(state.allow2 == true){
		if(state.triggerMode2 != null){
if(state.triggerMode2.contains(location.mode)){ 
LOGDEBUG("Mode is now $state.modeNow")   
LOGINFO("Stopping timer... ")
vDevice.stopTimer()
if(switch10){switch10.off()}
}}}}





def hsmStatusHandler(evt){
	
state.ofHSM = evt.value
state.reqStopHSM = stopHSM
state.reqStartHSM = startHSM
LOGDEBUG( "hsmStatusHandler - evt = $evt.value")
	
    if(state.reqStartHSM != null){
    
	if(state.allow2 == true){
if(state.ofHSM.contains(state.reqStopHSM)){
LOGDEBUG( "HSM changed to $state.ofHSM so stopping timer")
vDevice.stopTimer()
	if(switch10){switch10.off()}}}
	
	if(state.allow1 == true){
if(state.ofHSM.contains(state.reqStartHSM)){ 
LOGDEBUG( "HSM changed to $state.ofHSM so starting timer")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}}
    }
}
	
def lock1Handler(evt){
	if(state.allow1 == true){
state.lock1 = evt.value
state.lockMode1 = lockMode1
if(state.lockMode1 == true && state.lock1 == "locked"){
LOGINFO("<b>Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}	
if(state.lockMode1 == false && state.lock1 == "unlocked"){
LOGINFO("<b>Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}}}
	
def lock2Handler(evt){
	if(state.allow2 == true){
state.lock2 = evt.value
state.lockMode2 = lockMode2
if(state.lockMode2 == true && state.lock2 == "locked"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
if(switch10){switch10.off()}}
if(state.lockMode2 == false && state.lock2 == "unlocked"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
	if(switch10){switch10.off()}}}}
	
def contact1Handler(evt){
	if(state.allow1 == true){
state.contact1 = evt.value
state.contactMode1 = contactMode1
if(state.contactMode1 == true && state.contact1 == "open"){
LOGINFO("<b<Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}
if(state.contactMode1 == false && state.contact1 == "closed"){
LOGINFO("<b<Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}}}
	
def contact2Handler(evt){
	if(state.allow2 == true){
state.contact2 = evt.value
state.contactMode2 = contactMode2
if(state.contactMode2 == true && state.contact2 == "open"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
if(switch10){switch10.off()}}
if(state.contactMode2 == false && state.contact2 == "closed"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
	if(switch10){switch10.off()}}}}
	
def switch1Handler(evt){
	if(state.allow1 == true){
state.switch1 = evt.value
state.switchMode1 = switchMode1
if(state.switchMode1 == true && state.switch1 == "on"){
LOGINFO("<b<Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}
if(state.switchMode1 == false && state.switch1 == "off"){
LOGINFO("<b<Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}}}
	
def switch2Handler(evt){
	if(state.allow2 == true){
state.switch2 = evt.value
state.switchMode2 = switchMode2
if(state.switchMode2 == true && state.switch2 == "on"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
if(switch10){switch10.off()}}
if(state.switchMode2 == false && state.switch2 == "off"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
	if(switch10){switch10.off()}}}}
	
def motion1Handler(evt){
	if(state.allow1 == true){
state.motion1 = evt.value
state.motionMode1 = motionMode1
if(state.motionMode1 == true && state.motion1 == "active"){
LOGINFO("<b>Starting timer... </b>")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}
if(state.motionMode1 == false && state.motion1 == "inactive"){
LOGINFO("<b<Starting timer...</b> ")
	 if(countType == "Countdown Seconds"){
	vDevice.setCountDownSeconds(vDeviceCount)
vDevice.startTimer()
        if(switch10){switch10.on()}
    }
     if(countType == "Countdown Minutes"){
	vDevice.setCountDownMinutes(vDeviceCount)
vDevice.startTimerMinutes()
        if(switch10){switch10.on()}
    }
}}}
	
def motion2Handler(evt){
	if(state.allow2 == true){
state.motion2 = evt.value
state.motionMode2 = motionMode2
if(state.motionMode2 == true && state.motion2 == "active"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
if(switch10){switch10.off()}}
if(state.motionMode2 == false && state.motion2 == "inactive"){
LOGINFO("<b>Stopping timer...</b> ")
vDevice.stopTimer()
	if(switch10){switch10.off()}}}}
	


def version(){
	setDefaults()
	pauseOrNot()
	logCheck()
	resetBtnName()
	checkButtons()
  
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

def logCheck(){
    state.checkLog = logLevel
	if(state.checkLog == "INFO"){log.info "Informational Logging Enabled"}
	if(state.checkLog == "DEBUG & INFO"){log.info "Debug & Info Logging Enabled"}
	if(state.checkLog == "NONE"){log.info "Further Logging Disabled"}
	if(logLevel == "DEBUG & INFO") runIn(1800,logsDown)
}

def LOGDEBUG(txt){
	if(state.checkLog == "DEBUG & INFO"){
    try {
    	log.debug("(App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGDEBUG unable to output requested data! - DEBUG- ${txt}")
    }
  }		
}

def LOGINFO(txt){
	if(state.checkLog == "INFO" || state.checkLog == "DEBUG & INFO"){
    try {
    	log.info("(App Version: ${state.version}) - ${txt}") 
    } catch(ex) {
    	log.error("LOGINFO unable to output requested data! - INFO- ${txt}")
    }
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
       		LOGINFO("You are using the current version of this app")
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
    section(){ paragraph "$state.preCheckMessage <br><br>$state.agreementNotice"}
	
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
	def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
   }
    
def setVersion(){
		state.version = "1.3.0"	 
		state.InternalName = "SuperTileCountdownChild"
    	state.ExternalName = "Super Tile Time Child"
		state.preCheckMessage = "This app was designed to use a special Virtual Display device  to display time or a countdown on a dashboard tile"
		state.agreementNotice = "By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.<br>This agreement can be found on-line at: http://hubitat.uk/Software_License_Agreement.txt"
    	state.CobraAppCheck = "supertilecountdown.json"
		
}







	
