/**
 *  ****************  Presence Central.  ****************
 *
 *  Credits: I have to credit Brian Gudauskas (Reliable Presence) & Eric Roberts (Everyones Presence) for stealing some of their code for multiple presence sensor determinations
 *
 *
 *  Design Usage:
 *  This is the 'Child' app for presence automation
 *
 *
 *  Copyright 2018 - 2019 Andrew Parker
 *  
 *  This SmartApp is free!
 *  If you feel it's worth it then, donations to support development efforts are accepted via: 
 *
 *  Paypal at: https://www.paypal.me/smartcobra
 *  
 *
 *  If you find this app useful then it would be nice to get a 'shout out' on the forum! -  @Cobra
 *  Have an idea to make this app better?  - Please let me know :)
 *
 * 
 *
 *-------------------------------------------------------------------------------------------------------------------
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  If modifying this project, please keep the above header intact and add your comments/credits below - Thank you! -  @Cobra
 *
 *-------------------------------------------------------------------------------------------------------------------
 *
 *  Last Update: 22/02/2019
 *
 *  Changes:
 *
 *
 *  V3.6.0 - Added ability to NOT send volume command
 *  V3.5.1 - Debug group delay errors (thanks to @BorrisTheCat for notifying me about this)
 *  V3.5.0 - Added 'Speak A Message (Speech Synth)' as an output option
 *  V3.4.1 - debug presence delays
 *  V3.4.0 - Added ability to select minutes or seconds for arrival/departure delay
 *  V3.3.0 - Added additonal (2nd) switch for restriction & fixed other restriction bugs
 *  V3.2.0 - Added presence delay to help with momentary departure of some devices
 *  V3.1.2 - Debug update pushover message
 *  V3.1.1 - Debug presence restriction
 *  V3.1.0 - Added disable apps code
 *  V3.0.0 - Streamlined restrictions page to action faster if specific restrictions not used. 
 *  V2.9.0 - Moved update notification to parent
 *  V2.8.2 - Added legacy modeHandler as Hubitat does not remove it on code update
 *  V2.8.1 - Debug after Hubitat UI update
 *  V2.8.0 - New restrictions page & code cleanup
 *  V2.7.2 - Debug - Revised update check
 *  V2.7.1 - Debug
 *  V2.7.0 - Revised update checking and added 'pause' button
 *  V2.6.1 - Revised auto update checking and added a manual update check button
 *  V2.6.0 - Added 'restrict by current mode'
 *  V2.5.0 - code cleanup & Added remote version checking 
 *  V2.4.0 - Re-enabled TTS talking as an action option
 *  V2.3.1 - Added ability to configure Pushover priority from GUI
 *  V2.3.0 - Added 'PushOver' messaging option
 *  V2.2.0 - Debug SMS and added 5 slots for phone numbers
 *  V2.1.0 - Added ability to arm/disarm HSM
 *  V2.0.0 - Initial port to Hubitat - Slightly restricted feature list
 *  V1.4.0 - Added 'Present' if everyone is at home trigger
 *  V1.3.0 - Updated 'Control a Switch' to give further options
 *  V1.2.5 - debug - Typo issue with sunrise/sunset which caused the app to only work when switched on.
 *  V1.2.4 - added sunset/sunrise/ restrictions (with offset)
 *  V1.2.3 - Added 'Flash Lights' to available responses
 *  V1.2.2 - Moved 'restriction Options' to last page
 *	V1.2.1 - Changed restrictions from compulsory entries to optional entries 
 *  V1.2.0 - Added Locks & Doors to available responses
 *  V1.1.0 - Added enable/disable switching
 *  V1.0.1 - debug
 *  V1.0.0 - POC
 *
 *  
 *  
 */

 
definition(
    name: "Presence_Central_Child",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Child App for Presence Automation",
     category: "Fun & Social",

   
    
    parent: "Cobra:Presence Central",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )
    
    
preferences {
	section() {
	page name: "mainPage", title: "", install: false, uninstall: true, nextPage: "restrictionsPage"
	page name: "restrictionsPage", title: "", install: true, uninstall: true
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
	if(enableSwitch1){subscribe(enableSwitch1, "switch", switchEnable1)}
	if(enableSwitch2){subscribe(enableSwitch2, "switch", switchEnable2)}
	if(enableSwitchMode == null){enableSwitchMode = true} // ????
	if(restrictPresenceSensor){subscribe(restrictPresenceSensor, "presence", restrictPresenceSensorHandler)}
	if(restrictPresenceSensor1){subscribe(restrictPresenceSensor1, "presence", restrictPresence1SensorHandler)}
	if(sunriseSunset){astroCheck()}
	if(sunriseSunset){schedule("0 1 0 1/1 * ? *", astroCheck)} // checks sunrise/sunset change at 00.01am every day
    
  // App Specific subscriptions & settings below here   
 

	state.selection1 = trigger
	subscribe(location, "hsmStatus", statusHandler)
	if(doorContact1){subscribe(doorContact1, "contact", doorContactHandler)}
	
	if(presenceSensor1){
		LOGDEBUG( "Trigger is: '$trigger'")
		subscribe(presenceSensor1, "presence", singlePresenceHandler) 
	}
	if(presenceSensor2){
		LOGDEBUG( "Trigger is:  '$trigger'")
        setPresence1()
		subscribe(presenceSensor2, "presence", group1Handler) 
	}
	if(presenceSensor3){
		LOGDEBUG( "Trigger is:  '$trigger'")
        setPresence2()
		subscribe(presenceSensor3, "presence", group2Handler)
	} 
	if(presenceSensor4){
		LOGDEBUG( "Trigger is:  '$trigger'")
        schedule(checkTime, checkPresenceTimeNow)
    	state.privatePresence = 'present'
		subscribe(presenceSensor4, "presence", timePresenceHandler)
	}
	if(presenceSensor5){
		LOGDEBUG( "Trigger is:  '$trigger'")
        setPresence3()
		subscribe(presenceSensor5, "presence", group3Handler)
	} 

	if(delayArrival == false){state.presence = 0}
	if(delayDeparture == false){state.nopresence = 0}
    	 

   
}

def mainPage() {
	dynamicPage(name: "mainPage") {  
	preCheck()
		
    section() {
		triggerInput()
		presenceActions()
        outputActions()
        
	}    
  }
}





def restrictionsPage() {
    dynamicPage(name: "restrictionsPage") {
        section(){paragraph "<font size='+1'>App Restrictions</font> <br>These restrictions are optional <br>Any restriction you don't want to use, you can just leave blank or disabled"}
        section(){
		input "enableSwitchYes", "bool", title: "Enable restriction by external on/off switch(es)", required: true, defaultValue: false, submitOnChange: true
			if(enableSwitchYes){
			input "enableSwitch1", "capability.switch", title: "Select the first switch to Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch1){ input "enableSwitchMode1", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			input "enableSwitch2", "capability.switch", title: "Select a second switch to Enable/Disable this app", required: false, multiple: false, submitOnChange: true 
			if(enableSwitch2){ input "enableSwitchMode2", "bool", title: "Allow app to run only when this switch is On or Off", required: true, defaultValue: false, submitOnChange: true}
			}
		}
        section(){
		input "modesYes", "bool", title: "Enable restriction by current mode(s)", required: true, defaultValue: false, submitOnChange: true	
			if(modesYes){	
			input(name:"modes", type: "mode", title: "Allow actions when current mode is:", multiple: true, required: false)
			}
		}	
       	section(){
		input "timeYes", "bool", title: "Enable restriction by time", required: true, defaultValue: false, submitOnChange: true	
			if(timeYes){	
    	input "fromTime", "time", title: "Allow actions from", required: false
    	input "toTime", "time", title: "Allow actions until", required: false
        	}
		}
		section(){
		input "dayYes", "bool", title: "Enable restriction by day(s)", required: true, defaultValue: false, submitOnChange: true	
			if(dayYes){	
    	input "days", "enum", title: "Allow actions only on these days of the week", required: false, multiple: true, options: ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"]
        	}
		}
		section(){
		input "presenceYes", "bool", title: "Enable restriction by presence sensor(s)", required: true, defaultValue: false, submitOnChange: true	
			if(presenceYes){	
    	input "restrictPresenceSensor", "capability.presenceSensor", title: "Select presence sensor 1 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor){input "restrictPresenceAction", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
     	input "restrictPresenceSensor1", "capability.presenceSensor", title: "Select presence sensor 2 to restrict action", required: false, multiple: false, submitOnChange: true
    	if(restrictPresenceSensor1){input "restrictPresenceAction1", "bool", title: "On = Allow action only when someone is 'Present'  <br>Off = Allow action only when someone is 'NOT Present'  ", required: true, defaultValue: false}
   			}
		}	
		section(){
		input "sunrisesetYes", "bool", title: "Enable restriction by sunrise or sunset", required: true, defaultValue: false, submitOnChange: true	
			if(sunrisesetYes){
       	input "sunriseSunset", "enum", title: "Sunrise/Sunset Restriction", required: false, submitOnChange: true, options: ["Sunrise","Sunset"] 
		if(sunriseSunset == "Sunset"){	
       	input "sunsetOffsetValue", "number", title: "Optional Sunset Offset (Minutes)", required: false
		input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
		if(sunriseSunset == "Sunrise"){
    	input "sunriseOffsetValue", "number", title: "Optional Sunrise Offset (Minutes)", required: false
		input "sunriseOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
        	}
     	}
		}	
       
        section() {input "debugMode", "bool", title: "Enable debug logging", required: true, defaultValue: false}
		 section() {label title: "Enter a name for this automation", required: false}
    }
}

           






def triggerInput() {
   input "trigger", "enum", title: "How to trigger actions?", required: true, submitOnChange: true, options: ["Single Presence Sensor", "Group 1 (Anyone arrives or leaves = changed presence)","Group 2 ('Present' if ANYONE is at home - 'Not Present' only when EVERYONE leaves", "Group 3 ('Present' only if EVERYONE is at home - 'Not Present' if ANYONE leaves)", "Check for a presence sensor at a certain time"]
  
}

def presenceActions(){
		if (trigger) {
		state.selection1 = trigger
    
	if(state.selection1 == "Single Presence Sensor"){
	input "presenceSensor1", "capability.presenceSensor", title: "Select presence sensor to trigger action", required: false, multiple: false 
	delaySet()
	}
    
	if(state.selection1 == "Group 1 (Anyone arrives or leaves = changed presence)"){
	input "presenceSensor2", "capability.presenceSensor", title: "Select presence sensors to trigger action", multiple: true, required: false
	delaySet()
  	}
    
	if(state.selection1 == "Group 2 ('Present' if ANYONE is at home - 'Not Present' only when EVERYONE leaves"){
	input "presenceSensor3", "capability.presenceSensor", title: "Select presence sensors to trigger action", multiple: true, required: false
	delaySet()
    }
    
    if(state.selection1 == "Group 3 ('Present' only if EVERYONE is at home - 'Not Present' if ANYONE leaves)"){
	input "presenceSensor5", "capability.presenceSensor", title: "Select presence sensors to trigger action", multiple: true, required: false
	delaySet()
    }
    
    if(state.selection1 == "Check for a presence sensor at a certain time"){
    input "checkTime", "time", title: "Time to check presence ", required: true
	input "presenceSensor4", "capability.presenceSensor", title: "Select presence sensor to check", multiple: false, required: false
	
    }
    
	
			
 }
}
    
def delaySet(){
		
	input "delayArrival", "bool", title: "Enable Arrival Delay", required: true, defaultValue: false, submitOnChange: true
			if(delayArrival == true){
				input "arrivalMinSec", "bool", title: "Delay: On for Minutes - Off for seconds", required: true
				input "arrivalDelay", "number", title: "Delay before arrival registered", required: true}
			
			
	input "delayDeparture", "bool", title: "Enable Departure Delay", required: true, defaultValue: false, submitOnChange: true
			if(delayDeparture == true){
				input "departureMinSec", "bool", title: "Delay: On for Minutes - Off for seconds", required: true
				input "departureDelay", "number", title: "Delay before departure registered ", required: true}
 
}




def outputActions(){
input "presenceAction", "enum", title: "What to do?",required: true, submitOnChange: true, options: ["Control A Switch", "Change Mode",  "Control a Lock", "Send An SMS Message", "PushOver Message", "Speak A Message (Music Player)", "Speak A Message (Speech Synth)", "Set Safety Monitor Mode"]

    // Removed from 'action' options until active/re-coded  ********************************************************************************************************************************
    // , "Control a Door", "Flash Lights", 
    
    
if (presenceAction) {
    state.selection2 = presenceAction
    
    
    if(state.selection2 == "Flash Lights"){
    input "flashMode", "bool", title: " On = Flash when someone arrives \r\n Off = Flash when someone leaves", required: true, defaultValue: false
    input "switches", "capability.switch", title: "Flash these lights", multiple: true
	input "numFlashes", "number", title: "This number of times (default 3)", required: false
    input "onFor", "number", title: "On for (Milliseconds - default 1000)", required: false
	input "offFor", "number", title: "Off for (Milliseconds - default 1000)", required: false
    }
    
    else if(state.selection2 == "Control A Switch"){
     input "switch1", "capability.switch", title: "Select switch(s) to turn on/off", required: false, multiple: true 
     input "presenceSensor1Action1", "enum", title: "What action to take?",required: true, submitOnChange: true, options: ["On when arrived/present & Off when left/not present","Off when arrived/present & On when left/not present", "On when arrived/present (No Off)", "Off when arrived/present (No On)", "On when left/not present (No Off)", "Off when left/not present (No On)"]

    }
      else if(state.selection2 == "PushOver Message"){ 
    input "speaker", "capability.speechSynthesis", title: "PushOver Device", required: false, multiple: true
    input "message1", "text", title: "Message to send when sensor arrives (Or is present at check time)",  required: false
    input "priority1", "enum", title: "Message Priority for Arrival",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
	input "message2", "text", title: "Message to send when sensor leaves (Or is not present at check time)",  required: false
    input "priority2", "enum", title: "Message Priority for Departure",required: true, submitOnChange: true, options: ["None", "Low", "Normal", "High"], defaultValue: "None"
//    input "msgDelay", "number", title: "Minutes delay between messages (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true

	}
 
    else if(state.selection2 == "Speak A Message (Music Player)"){ 
    input "speaker", "capability.musicPlayer", title: "Choose speaker(s)", required: false, multiple: true
	input "doVolume", "bool", title: "Set Volume before speaking", required: true, defaultValue: true, submitOnChange: true
		if(doVolume == true){ 
	input "volume1", "number", title: "Normal Speaker volume", description: "0-100%", defaultValue: "100",  required: true
	input "volume2", "number", title: "Quiet Time Speaker volume", description: "0-100%", defaultValue: "0",  required: true
    input "fromTime2", "time", title: "Quiet Time Start", required: false
    input "toTime2", "time", title: "Quiet Time End", required: false
		}
    input "message1", "text", title: "Message to play when sensor arrives (Or is present at check time)",  required: false
	input "message2", "text", title: "Message to play when sensor leaves (Or is not present at check time)",  required: false
    input "msgDelay", "number", title: "Minutes delay between messages (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
	
	}
    else if(state.selection2 == "Speak A Message (Speech Synth)"){ 
    input "speaker", "capability.speechSynthesis", title: "Choose speaker(s)", required: false, multiple: true
		input "doVolume", "bool", title: "Set Volume before speaking", required: true, defaultValue: true, submitOnChange: true
		if(doVolume == true){ 
	input "volume1", "number", title: "Normal Speaker volume", description: "0-100%", defaultValue: "100",  required: true
	input "volume2", "number", title: "Quiet Time Speaker volume", description: "0-100%", defaultValue: "0",  required: true
    input "fromTime2", "time", title: "Quiet Time Start", required: false
    input "toTime2", "time", title: "Quiet Time End", required: false
		}
    input "message1", "text", title: "Message to play when sensor arrives (Or is present at check time)",  required: false
	input "message2", "text", title: "Message to play when sensor leaves (Or is not present at check time)",  required: false
    input "msgDelay", "number", title: "Minutes delay between messages (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
	
	}
    
     else if(state.selection2 == "Send An SMS Message"){
     input "message1", "text", title: "Message to send when sensor arrives  (Or is present at check time)",  required: false
	 input "message2", "text", title: "Message to send when sensor leaves  (Or is not present at check time)",  required: false

     	input(name: "sms1", type: "phone", title: "Input 1st Phone Number ", required: false)
        input(name: "sms2", type: "phone", title: "Input 2nd Phone Number ", required: false)
        input(name: "sms3", type: "phone", title: "Input 3rd Phone Number ", required: false)
        input(name: "sms4", type: "phone", title: "Input 4th Phone Number ", required: false)
        input(name: "sms5", type: "phone", title: "Input 5th Phone Number ", required: false)
      
     }
 //    }
    
    else if(state.selection2 == "Change Mode"){
    input "newMode1", "mode", title: "Change to this mode when someone arrives (Or is present at check time)",  required: false
    input "newMode2", "mode", title: "Change to this mode when someone leaves (Or is not present at check time)",  required: false
    
    }
    
     else if(state.selection2 == "Run a Routine"){
      def actions = location.helloHome?.getPhrases()*.label
            if (actions) {
            input "routine1", "enum", title: "Select a routine to execute when someone arrives (Or is present at check time)", required: false, options: actions
            input "routine2", "enum", title: "Select a routine to execute when someone leaves (Or is not present at check time)" , required: false, options: actions
                    }
            }
    
     else if(state.selection2 == "Control a Door"){
     input "doorAction", "enum", title: "How to control door",required: true, submitOnChange: true, options: ["Single Momentary Switch", "Two Momentary Switch(es)", "Open/Close Door"]
     		selectDoorActions()
           }
           
     else if(state.selection2 == "Control a Lock"){
      		input "lock1", "capability.lock", title: "Select lock(s) ", required: false, multiple: true 
      		input "lockDelay", "number", title: "Minutes delay between actions (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
           }
    
    
    
    
    
	}
}    

def selectDoorActions(){
if(doorAction){
    state.actionOnDoor = doorAction
    
    if(state.actionOnDoor == "Two Momentary Switch(es)"){
    input "doorSwitch1", "capability.switch", title: "Switch to open", required: false, multiple: true
    input "doorSwitch2", "capability.switch", title: "Switch to close", required: false, multiple: true
    input "doorMomentaryDelay", "number", title: "How many seconds to hold switch on", defaultValue: '1', description: "Seconds", required: true
    input "doorDelay", "number", title: "Minutes delay between actions (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
	input "doorContact1", "capability.contactSensor", title: "Door Contact (Optional)", required: false, multiple: false, submitOnChange:true
    if(doorContact1){
    input(name: "alertOption", type: "bool", title: "Send a notification when contact open/closed", description: null, defaultValue: false, submitOnChange:true)
    }
    if(alertOption == true){
       
		input "doorContactDelay", "number", title: "Once contact has been open/closed for this number of seconds", defaultValue: '0', description: "Seconds", required: true
  //      input("recipients", "contact", title: "Send notifications to") {

            
            
    //	input(name: "pushNotification", type: "bool", title: "Send a push notification", description: null, defaultValue: true)
      //  }
    	input "message1", "text", title: "Send this message (Open)",  required: false
    	input "message2", "text", title: "Send this message (Closed)",  required: false
        input(name: "sms1", type: "phone", title: "Input 1st Phone Number ", required: false)
        input(name: "sms2", type: "phone", title: "Input 2nd Phone Number ", required: false)
        input(name: "sms3", type: "phone", title: "Input 3rd Phone Number ", required: false)
        input(name: "sms4", type: "phone", title: "Input 4th Phone Number ", required: false)
        input(name: "sms5", type: "phone", title: "Input 5th Phone Number ", required: false)
        
     }
    }
    
    else if(state.actionOnDoor == "Open/Close Door"){
    input "door1", "capability.doorControl", title: "Select door to open/close", required: false, multiple: true 
    input "doorDelay", "number", title: "Minutes delay between actions (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
    input "doorContact1", "capability.contactSensor", title: "Door Contact (Optional)", required: false, multiple: false, submitOnChange:true
    if(doorContact1){
    input(name: "alertOption", type: "bool", title: "Send a notification when contact open/closed", description: null, defaultValue: false, submitOnChange:true)
    }
    if(alertOption == true){
       
		input "doorContactDelay", "number", title: "Once contact has been open/closed for this number of seconds", defaultValue: '0', description: "Seconds", required: true
   //     input("recipients", "contact", title: "Send notifications to") {
   // 	input(name: "sms", type: "phone", title: "Send A Text To", description: null, required: false)
   // 	input(name: "pushNotification", type: "bool", title: "Send a push notification", description: null, defaultValue: true)
     //   }
    	input "message1", "text", title: "Send this message (Open)",  required: false
    	input "message2", "text", title: "Send this message (Closed)",  required: false
        input(name: "sms1", type: "phone", title: "Input 1st Phone Number ", required: false)
        input(name: "sms2", type: "phone", title: "Input 2nd Phone Number ", required: false)
        input(name: "sms3", type: "phone", title: "Input 3rd Phone Number ", required: false)
        input(name: "sms4", type: "phone", title: "Input 4th Phone Number ", required: false)
        input(name: "sms5", type: "phone", title: "Input 5th Phone Number ", required: false)
     }
}
    
    else  if(state.actionOnDoor == "Single Momentary Switch"){
    input "doorSwitch1", "capability.switch", title: "Switch to open/close", required: false, multiple: true 
   	input "doorMomentaryDelay", "number", title: "How many seconds to hold switch on", defaultValue: '1', description: "Seconds", required: true
    input "doorDelay", "number", title: "Minutes delay between actions (Enter 0 for no delay)", defaultValue: '0', description: "Minutes", required: true
	input "doorContact1", "capability.contactSensor", title: "Door Contact (Optional)", required: false, multiple: false, submitOnChange:true 
    if(doorContact1){
    input(name: "alertOption", type: "bool", title: "Send a notification when contact open/closed", description: null, defaultValue: false, submitOnChange:true)
    }
    if(alertOption == true){
       
		input "doorContactDelay", "number", title: "Once contact has been open/closed for this number of seconds", defaultValue: '0', description: "Seconds", required: true
   //     input("recipients", "contact", title: "Send notifications to") {
    //	input(name: "sms", type: "phone", title: "Send A Text To", description: null, required: false)
   // 	input(name: "pushNotification", type: "bool", title: "Send a push notification", description: null, defaultValue: true)
   //     }
    	input "message1", "text", title: "Send this message (Open)",  required: false
    	input "message2", "text", title: "Send this message (Closed)",  required: false
        input(name: "sms1", type: "phone", title: "Input 1st Phone Number ", required: false)
        input(name: "sms2", type: "phone", title: "Input 2nd Phone Number ", required: false)
        input(name: "sms3", type: "phone", title: "Input 3rd Phone Number ", required: false)
        input(name: "sms4", type: "phone", title: "Input 4th Phone Number ", required: false)
        input(name: "sms5", type: "phone", title: "Input 5th Phone Number ", required: false)
        
     }
  }  
}
}
// ************************ Handlers ****************************************

def modeHandler(evt){
// Legacy	
	unsubscribe(modeHandler) 
}


def checkDelaySetting(){
	if(delayArrival == false){
	LOGDEBUG("No arrival delay configured. Continue...")	
	state.presence = 0
	}
	
	if(delayArrival == true){
		if(arrivalMinSec == true){
		state.presence = (60 * arrivalDelay)
		}
		if(arrivalMinSec == false){
		state.presence = arrivalDelay
		}
		
	LOGDEBUG("Arrival delay configured: $state.presence seconds")	
		
	}
	
	if(delayDeparture == false){
	LOGDEBUG("No departure delay configured. Continue...")	
	state.nopresence = 0
	}
	
	if(delayDeparture == true){
		if(departureMinSec == true){
		state.nopresence = (60 * departureDelay)
		LOGDEBUG("Departure delay configured: $state.nopresence seconds")
	}	
	if(departureMinSec == false){
		state.nopresence = departureDelay
		LOGDEBUG("Departure delay configured: $state.nopresence seconds")
	}	
  }
}

// Single Presence =============================================================

def singlePresenceHandler(evt){
	checkDelaySetting()
	
	state.privatePresence = evt.value
if (state.privatePresence == "present"){
	LOGDEBUG("Checking presence again in $state.presence seconds")
	runIn(state.presence, checkPresenceAgain)

}
if (state.privatePresence == "not present"){
	LOGDEBUG("Checking presence again in $state.nopresence seconds")
runIn(state.nopresence, checkPresenceAgain)
}
}



def checkPresenceAgain(){
	if (state.privatePresence == "present"){arrivalAction()}
	if (state.privatePresence == "not present"){departureAction()}
}

// end single presence =========================================================

// Timed Presence Check =========================================================



def checkPresenceTimeNow(evt){
LOGDEBUG("Activating timed check now.... ($checkTime)")

if (state.privatePresence == "present"){
checkPresenceAgain()
}
if (state.privatePresence == "not present"){
checkPresenceAgain()
}
}



def timePresenceHandler(evt){
state.privatePresence = evt.value
LOGDEBUG("state.privatePresence = $state.privatePresence")




}

// end timed presence check =====================================================



// Group 1  ======================================================================
def group1Handler(evt) {
checkDelaySetting()
	
    if (evt.value == "present") {
        if (state.privatePresence1 != "present") {
            state.privatePresence1 = "present"
            state.privatePresence = "present"
           LOGDEBUG("A sensor arrived so setting group to '$state.privatePresence'")
           runIn(state.presence, checkPresenceAgain)
            }
    } else if (evt.value == "not present") {
        if (state.privatePresence1 != "not present") {
            state.privatePresence1 = "not present"
            state.privatePresence = "not present"
            LOGDEBUG("A sensor left so setting group to '$state.privatePresence'")
            runIn(state.nopresence, checkPresenceAgain)
        }
    }
}


// end group 1 ========================================================


// Group 2 ============================================================

def group2Handler(evt) {
	setPresence2()
}


// end group 2 ========================================================
// Group 3 ============================================================

def group3Handler(evt) {
	setPresence3()
}


// end group 3 ========================================================


// Door Contact Handler

def doorContactHandler(evt){
state.contactDoor = evt.value
LOGDEBUG("state.contactDoor = $state.contactDoor")



}

// end handlers *************************************************************



// ************************* Actions ****************************************

// HSM Actions

def statusHandler(evt){
  state.hsmStatus = evt.value
  LOGDEBUG("HSM = $state.hsmStatus")
      
}


// Flash Actions

def checkFlashArrived(){
if (flashMode == true){
flashLights()
}
}
def checkFlashDeparted(){
if (flashMode == false){
flashLights()
}
}






private flashLights() {
    checkAllow()
	if(state.allAllow == true){

	def doFlash = true
	def onFor = onFor ?: 1000
	def offFor = offFor ?: 1000
	def numFlashes = numFlashes ?: 3

	LOGDEBUG("LAST ACTIVATED IS: ${state.lastActivated}")
	if (state.lastActivated) {
		def elapsed = now() - state.lastActivated
		def sequenceTime = (numFlashes + 1) * (onFor + offFor)
		doFlash = elapsed > sequenceTime
		LOGDEBUG("DO FLASH: $doFlash, ELAPSED: $elapsed, LAST ACTIVATED: ${state.lastActivated}")
	}

	if (doFlash) {
		LOGDEBUG("FLASHING $numFlashes times")
		state.lastActivated = now()
		LOGDEBUG("LAST ACTIVATED SET TO: ${state.lastActivated}")
		def initialActionOn = switches.collect{it.currentSwitch != "on"}
		def delay = 0L
		numFlashes.times {
			LOGDEBUG("Switch on after  $delay msec")
			switches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.on(delay: delay)
				}
				else {
					s.off(delay:delay)
				}
			}
			delay += onFor
			LOGDEBUG("Switch off after $delay msec")
			switches.eachWithIndex {s, i ->
				if (initialActionOn[i]) {
					s.off(delay: delay)
				}
				else {
					s.on(delay:delay)
				}
			}
			delay += offFor
		}
	}
}
}

// Arrival Actions - Check OK to run
def arrivalAction(){
    LOGDEBUG("Calling Arrival Action")
	checkAllow()
	if(state.allAllow == true){decideActionArrival()}
}


// Departure Actions - Check OK to run
def departureAction(){
LOGDEBUG("Calling Departure Action")
	checkAllow()
	if(state.allAllow == true){decideActionDeparture()}
}





// Decide which action to call
def decideActionArrival() {
    LOGDEBUG("Deciding on correct Arrival Action")

 if(state.selection2 == "Control A Switch"){
  LOGDEBUG("Decided to: 'Control A Switch' ")




 if(presenceSensor1Action1){
   state.actionType1 = presenceSensor1Action1
   
   if (state.actionType1 == "On when arrived/present & Off when left/not present"){
   LOGDEBUG(" Action Type = On when arrived/present & Off when left/not present")
   LOGDEBUG("Switching on...")
		switch1.on()
      }
   else if (state.actionType1 == "Off when arrived/present & On when left/not present"){
   LOGDEBUG(" Action Type = Off when arrived/present & On when left/not present")
   LOGDEBUG("Switching off...")
		switch1.off()
      } 
      
   else if (state.actionType1 == "On when arrived/present (No Off)"){
   LOGDEBUG(" Action Type = On when arrived/present (No Off)")
   LOGDEBUG("Switching on...")
		switch1.on()
      } 
   else if (state.actionType1 == "Off when arrived/present (No On)"){
   LOGDEBUG(" Action Type = Off when arrived/present (No On)")
   LOGDEBUG("Switching off...")
		switch1.off()
      }   
      
	}  
 }
 
  else if(state.selection2 == "PushOver Message"){
  LOGDEBUG("Decided to: Send a message via PushOver - Sending now... ")
	pushOver('arrived')
   
 }
    
    
  else if(state.selection2 == "Speak A Message (Music Player)"){
  LOGDEBUG("Decided to: 'Speak A Message (Music Player)' ")
  state.msg1 = message1
	speakNow()
 }
	
  else if(state.selection2 == "Speak A Message (Speech Synth)"){
  LOGDEBUG("Decided to: 'Speak A Message (Speech Synth)' ")
  state.msg1 = message1
	speakNow()
 }	
  else if(state.selection2 == "Set Safety Monitor Mode"){
 LOGDEBUG("Decided to: 'Change HSM mode to Disarm' (intruder alerts only")
 sendLocationEvent(name: "hsmSetArm", value: "disarm")
      
 }
    
 else if(state.selection2 == "Send An SMS Message"){
 LOGDEBUG("Decided to: 'Send An SMS Message' ")
 def msg = message1
  sendMessage(msg)
 }

 else if(state.selection2 == "Change Mode"){
   LOGDEBUG("Decided to: 'Change Mode'")
 if(newMode1){
 changeMode1()
 }
 }

 else if(state.selection2 == "Run a Routine"){
 LOGDEBUG("Decided to: 'Run a Routine' ") 
 state.routineGo = routine1
 LOGDEBUG("Running routine: $state.routineGo")
 location.helloHome?.execute(state.routineGo)
 }
 
 
  else if(state.selection2 == "Control a Door"){
  LOGDEBUG("Decided to: 'Control a Door' ") 
   if(state.actionOnDoor == "Two Momentary Switch(es)"){
   LOGDEBUG("Using 'two momentary' switching") 
    switchDoorOn()   
   }
   else if(state.actionOnDoor == "Single Momentary Switch"){
    LOGDEBUG("Using single momentary switching")  
   switchDoorOn2()
  } 
  else if(state.actionOnDoor == "Open/Close Door"){
  LOGDEBUG("Using standard door switching")
  LOGDEBUG("Opening door....")
	openDoorNow()
    }
 }
 
 else if(state.selection2 == "Control a Lock"){
  LOGDEBUG("Decided to: 'Control a Lock' ") 
	openLock()
 }
 else if(state.selection2 == "Flash Lights"){
  LOGDEBUG("Decided to: 'Flash Lights' ")
checkFlashArrived()
  
 }
 
}






def decideActionDeparture() {
LOGDEBUG("Deciding on correct Departure Action")

 if(state.selection2 == "Control A Switch"){
 LOGDEBUG("Decided to: 'Control A Switch' ")
 
 
 // "On when arrived/present & Off when left/not present","Off when arrived/present & On when left/not present", "On when arrived/present (No Off)", "Off when arrived/present (No On)", "On when left/not present (No Off)", "Off when left/not present (No On)"]
 
 if(presenceSensor1Action1){
   state.actionType1 = presenceSensor1Action1
   
   if (state.actionType1 == "On when arrived/present & Off when left/not present"){
   LOGDEBUG(" Action Type = On when arrived/present & Off when left/not present")
   LOGDEBUG("Switching off...")
		switch1.off()
      }
   else if (state.actionType1 == "Off when arrived/present & On when left/not present"){
   LOGDEBUG(" Action Type = Off when arrived/present & On when left/not present")
   LOGDEBUG("Switching on...")
		switch1.on()
      } 
      
   else if (state.actionType1 == "On when left/not present (No Off)"){
   LOGDEBUG(" Action Type = On when left/not present (No Off)")
   LOGDEBUG("Switching on...")
		switch1.on()
      } 
   else if (state.actionType1 == "Off when left/not present (No On)"){
   LOGDEBUG(" Action Type = Off when left/not present (No On)")
   LOGDEBUG("Switching off...")
		switch1.off()
      }   
      
	}  
 }
    
else if(state.selection2 == "PushOver Message"){
  LOGDEBUG("Decided to: Send a message via PushOver - Sending now... ")
	pushOver('departed')
 }    
    
else if(state.selection2 == "Speak A Message (Music Player)"){
  LOGDEBUG("Decided to: 'Speak A Message (Music Player)' ")
  state.msg1 = message2
	speakNow()
 }
	
  else if(state.selection2 == "Speak A Message (Speech Synth)"){
  LOGDEBUG("Decided to: 'Speak A Message (Speech Synth)' ")
  state.msg1 = message2
	speakNow()
 }	
	
 else if(state.selection2 == "Send An SMS Message"){
 LOGDEBUG("Decided to: 'Send An SMS Message' ")
   def msg = message2
  sendMessage(msg)
 }
    
  else if(state.selection2 == "Set Safety Monitor Mode"){
 LOGDEBUG("Decided to: 'Change HSM mode to ArmAway' ")
 sendLocationEvent(name: "hsmSetArm", value: "armAway")
      
 }

 else if(state.selection2 == "Change Mode"){
  LOGDEBUG("Decided to: 'Change Mode'")
 if(newMode2){
 changeMode2()
 }
 }

 else if(state.selection2 == "Run a Routine"){
 LOGDEBUG("Decided to: 'Run a Routine' ")
   state.routineGo = routine2
 LOGDEBUG("Running routine: $state.routineGo")
 location.helloHome?.execute(state.routineGo)
 
 }
 
  else if(state.selection2 == "Control a Door"){
  LOGDEBUG("Decided to: 'Control a Door' ")
  
  if(state.actionOnDoor == "Two Momentary Switch(es)"){
   LOGDEBUG("Using 'two momentary' switching") 
   switchDoorOff()
   }
   else if(state.actionOnDoor == "Single Momentary Switch"){
    LOGDEBUG("Using single momentary switching") 
   switchDoorOff2()
  } 
   else if(state.actionOnDoor == "Open/Close Door"){
   LOGDEBUG("Using standard door switching")
   LOGDEBUG("Closing door....")
	closeDoorNow()
    }
  
 
}
  else if(state.selection2 == "Control a Lock"){
  LOGDEBUG("Decided to: 'Control a Lock' ") 
  secureLock()
  
}
 else if(state.selection2 == "Flash Lights"){
  LOGDEBUG("Decided to: 'Flash Lights' ")
checkFlashDeparted()
  
}
 
}







// Group 1 Actions ======================================

def setPresence1(){
checkDelaySetting()
	def presentCounter1 = 0
    
    presenceSensor2.each {
    	if (it.currentValue("presence") == "present") {
        	presentCounter1++
        }
    }
    
    log.debug("presentCounter1: ${presentCounter1}")
    
    if (presentCounter1 > 0) {
    	if (state.privatePresence1 != "present") {
    		state.privatePresence1 = "present"
            state.privatePresence = "present"
            log.debug("A sensor arrived so setting group to '$state.privatePresence'")
			runIn(state.presence, checkPresenceAgain)
        }
    } else {
    	if (state.privatePresence1 != "not present") {
    		state.privatePresence1 = "not present"
            state.privatePresence = "not present"
            log.debug("A sensor left so setting group to '$state.privatePresence'")
			runIn(state.nopresence, checkPresenceAgain)
        }
    }
}

// end group 1 actions ==================================

// Group 2 Actions ======================================

def setPresence2(){

def	presentCounter2 = 0
 checkDelaySetting()
	
			def home = presenceSensor3.findAll { it?.currentValue("presence") == 'present' }
			def sensorsPresent = "${home.join(',')}"
			log.info "Sensors Present = $sensorsPresent"
	
			def away = presenceSensor3.findAll { it?.currentValue("presence") == 'not present' }
			def sensorsNotPresent = "${away.join(',')}"
			log.info "Sensors Not Present = $sensorsNotPresent"
	
        presenceSensor3.each {
    	if (it.currentValue("presence") == "present") {
        	presentCounter2++
        }
    }
    
    log.debug("Number of sensors present: ${presentCounter2}")
    
    if (presentCounter2 > 0) {
    	if (state.privatePresence2 != "present") {
            state.privatePresence2 = "present"
            state.privatePresence = "present"
			log.debug("Arrived - At least one sensor arrived - set group to '$state.privatePresence'")
             runIn(state.presence, checkPresenceAgain)
        }
    } else {
    	if (state.privatePresence2 != "not present") {
            state.privatePresence2 = "not present"
            state.privatePresence = "not present"
            log.debug("Departed - Last sensor left - set group to '$state.privatePresence'")
             runIn(state.nopresence, checkPresenceAgain)
        }
    }
}

// end group 2 actions ==================================


// Group 3 Actions ======================================

def setPresence3(){
checkDelaySetting()
def	presentCounter3 = 0
	
	def home = presenceSensor5.findAll { it?.currentValue("presence") == 'present' }
			def sensorsPresent = "${home.join(',')}"
			log.info "Sensors Present = $sensorsPresent"
	
			def away = presenceSensor5.findAll { it?.currentValue("presence") == 'not present' }
			def sensorsNotPresent = "${away.join(',')}"
			log.info "Sensors Not Present = $sensorsNotPresent"
	
	
        presenceSensor5.each {
    	if (it.currentValue("presence") == "not present") {
        	presentCounter3++
        }
    }
    
//    log.debug("Number of sensors NOT present: ${presentCounter3}")
    
    if (presentCounter3 > 0) {
    	if (state.privatePresence3 != "not present") {
            state.privatePresence3 = "not present"
            state.privatePresence = "not present"
            log.debug("Arrived - At least one sensor left - set group to '$state.privatePresence'")
            runIn(state.nopresence, checkPresenceAgain)
             
        }
    } else {
    	if (state.privatePresence3 != "present") {
            state.privatePresence3 = "present"
            state.privatePresence = "present"
            log.debug("Departed - All sensors present - set group to '$state.privatePresence'")
			log.warn "state.nopresence = $state.nopresence"
            runIn(state.presence, checkPresenceAgain)
        }
    }
}

// end group 3 actions ==================================

// PushOver Message Actions =============================
def pushOver(status){
 def type1 = status

 if(type1 == 'arrived'){
  def newPriority = priority1 
  LOGDEBUG("Message priority = $newPriority - Action = $status" ) 
     
    if(newPriority  == 'None'){
     state.msg1 = message1
	speaker.speak(state.msg1)
    }
     
    else if(newPriority  == 'Low'){
     state.msg1 = '[L]' + message1
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'Normal'){
     state.msg1 = '[N]' + message1
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'High'){
     state.msg1 = '[H]' + message1
	speaker.speak(state.msg1)
    }
  } 
 if(type1 == 'departed'){
  def newPriority = priority2 
  LOGDEBUG("Message priority = $newPriority - Action = $status" ) 
     
    if(newPriority  == 'None'){
     state.msg1 = message2
	speaker.speak(state.msg1)
    }
     
    else if(newPriority  == 'Low'){
     state.msg1 = '[L]' + message2
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'Normal'){
     state.msg1 = '[N]' + message2
	speaker.speak(state.msg1)
    }
    else if(newPriority  == 'High'){
     state.msg1 = '[H]' + message2
	speaker.speak(state.msg1)
    }
  }     
    
}




// Mode Actions  ======================================

def changeMode1() {
    LOGDEBUG( "changeMode1, location.mode = $location.mode, newMode1 = $newMode1, location.modes = $location.modes")
 	setLocationMode(newMode1) //inserted for Hubitat

}
def changeMode2() {
    LOGDEBUG( "changeMode2, location.mode = $location.mode, newMode2 = $newMode2, location.modes = $location.modes")
 setLocationMode(newMode2)   //inserted for Hubitat
 
}

// end mode actions =================================


// Message Actions ==================================


def sendMessage(msg) {
    
  LOGDEBUG("Sending message: '$msg' to configured phone numbers")

     if (sms1) {
          LOGDEBUG("Sending message: '$msg' to $sms1")
         sendSms(sms1, msg)
     }
     if (sms2) {
          LOGDEBUG("Sending message: '$msg' to $sms2")
         sendSms(sms2, msg)
     }
     if (sms3) { 
         LOGDEBUG("Sending message: '$msg' to $sms3")
                sendSms(sms3, msg)
               }
     if (sms4) {
          LOGDEBUG("Sending message: '$msg' to $sms4")
         sendSms(sms4, msg)
     }
     if (sms5) {
         LOGDEBUG("Sending message: '$msg' to $sms5")
         sendSms(sms5, msg)
     }
    

}

// end message actions ===============================

// Speaking Actions ==================================

def speakNow(){
LOGDEBUG("speakNow called...")
checkVolume()

    if ( state.timer1 == true && state.msg1 != null){
	LOGDEBUG("Speaking now - Message: '$state.msg1'")
		if(state.selection2 == "Speak A Message (Music Player)"){speaker.playTextAndRestore(state.msg1)}
		if(state.selection2 == "Speak A Message (Speech Synth)"){speaker.speak(state.msg1)}
   	startTimerSpeak()  
 } 
	else if ( state.timer1 == false){
	LOGDEBUG("NOT Speaking now - Too close to last message so I have to wait a while before I can speak again...")
 }
 	else if(state.msg1 == null){
    LOGDEBUG("No message configured")
    
    }
}

def startTimerSpeak(){
state.timer1 = false
state.timeDelay = 60 * msgDelay
LOGDEBUG("Waiting for $msgDelay minutes before resetting timer to allow further messages")
runIn(state.timeDelay, resetTimerSpeak)
}

def resetTimerSpeak() {
state.timer1 = true
LOGDEBUG( "Timer reset - Messages allowed again...")
}


// end speaking actions ==============================



// Door Actions ======================================
def openDoorNow(){
LOGDEBUG( "calling openDoorNow")
if(state.timerDoor == true){
 		if(state.contactDoor != 'open'){
			LOGDEBUG("Door is closed...")
            LOGDEBUG( "Opening door...")
        if(message1){
            def messageDelay = doorContactDelay
        	state.contactMsg = message1
            runIn(messageDelay, runContactMsg)
            }
			door1.open()
            startTimerDoor()
            }
  else if(state.contactDoor == 'open'){
          LOGDEBUG("Door already open!")
}            
            }
           
if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}


def closeDoorNow(){
LOGDEBUG( "calling closeDoorNow")
if(state.timerDoor == true){
			LOGDEBUG("Door is open...")
            LOGDEBUG("Closing door...")
			door1.close()
            if(message2){
            def messageDelay = doorContactDelay
        	state.contactMsg = message2
            runIn(messageDelay, runContactMsg)
            }
            startTimerDoor()
            }
 else if(state.contactDoor == 'open'){
          LOGDEBUG("Door already open!")
}

if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}


def startTimerDoor(){
LOGDEBUG("calling startTimerDoor")
state.timerDoor = false
def doorDelay1 = 60 * doorDelay as int
LOGDEBUG("Waiting for $doorDelay minutes before resetting timer to allow further actions")
runIn(doorDelay1, resetTimerDoor1)
}

def resetTimerDoor1() {
LOGDEBUG("calling resetTimerdoor1")
state.timerDoor = true
LOGDEBUG( "Timer reset - Actions allowed again...")
}


def switchDoorOn(){
LOGDEBUG( "calling switchDoorOn")
	if(state.timerDoor == true){
		def	momentaryTime1 = doorMomentaryDelay as int
       
        if(state.contactDoor != 'open'){
			LOGDEBUG("Opening door....")
	  		doorOn1() 
	  		runIn(momentaryTime1, doorOff1)
            if(message1){
            def messageDelay = doorContactDelay
        	state.contactMsg = message1
            runIn(messageDelay, runContactMsg)
            }
			startTimerDoor()
            }
         else if(state.contactDoor == 'open'){
          LOGDEBUG("Door already open!")
}
	else if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}
}
def switchDoorOff(){
LOGDEBUG( "calling switchDoorOff")
	if(state.timerDoor == true){
		def	momentaryTime1 = doorMomentaryDelay as int
         if(state.contactDoor != 'closed'){
			LOGDEBUG("Closing door....")
			doorOn2() 
			runIn(momentaryTime1, doorOff2)
             if(message2){
            def messageDelay = doorContactDelay
        	state.contactMsg = message2
            runIn(messageDelay, runContactMsg)
            }
			startTimerDoor()
            }
         else if(state.contactDoor == 'closed'){
           LOGDEBUG("Door already closed!")
         }
}
	else if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}

def switchDoorOn2(){
LOGDEBUG( "calling switchDoorOn")
	if(state.timerDoor == true){
		def	momentaryTime1 = doorMomentaryDelay as int
        if(state.contactDoor != 'open'){
			LOGDEBUG("Opening door....")
	  		doorOn1() 
	  		runIn(momentaryTime1, doorOff1)
             if(message1){
            def messageDelay = doorContactDelay
        	state.contactMsg = message1
            RunIn(messageDelay, runContactMsg)
            }
			startTimerDoor()
            }
           else if(state.contactDoor == 'open'){
           LOGDEBUG("Door already open!")
         } 
}
	else if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}

def switchDoorOff2(){
LOGDEBUG( "calling switchDoorOff2")
	if(state.timerDoor == true){
		def	momentaryTime1 = doorMomentaryDelay as int
         if(state.contactDoor != 'closed'){
			LOGDEBUG("Closing door....")
			doorOn1() 
			runIn(momentaryTime1, doorOff1)
             if(message2){
            def messageDelay = doorContactDelay
        	state.contactMsg = message2
            RunIn(messageDelay, runContactMsg)
            }
			startTimerDoor()
            }
           else if(state.contactDoor == 'closed'){
           LOGDEBUG("Door already closed!")
         }
}
	else if(state.timerDoor == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}



def doorOn1(){doorSwitch1.on()}
def doorOff1(){doorSwitch1.off()}
def doorOn2(){doorSwitch2.on()}
def doorOff2(){doorSwitch2.off()}

def runContactMsg(){
LOGDEBUG("runContactMsg - Sending message...")
def msg = state.contactMsg
  sendMessage(msg)

}


// end door actions ==================================


// Lock Actions ======================================

def secureLock(){
LOGDEBUG( "Securing Lock(s)")
if(state.timerlock == true){
// def anyLocked = lock1.count{it.currentLock == "unlocked"} != lock1.size()
//			if (anyLocked) {
			lock1.lock()
            LOGDEBUG("Locked")
            startTimerLock()
//            }
}
if(state.timerLock == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}

def openLock(){
LOGDEBUG( "Opening Lock(s)")
if(state.timerlock == true){
//	def anyUnlocked = lock1.count{it.currentLock == "locked"} != lock1.size()
//			if (anyUnlocked) {
			lock1.unlock()
            LOGDEBUG("Unlocked")
            startTimerLock()
//            }
}
if(state.timerLock == false){
LOGDEBUG("Too soon since last action to do anything - I need to wait for the timer to expire")
}
}


def startTimerLock(){
state.timerlock = false
state.timeDelayLock = 60 * lockDelay as int
LOGDEBUG("Waiting for $lockDelay minutes before resetting timer to allow further actions")
runIn(state.timeDelayLock, resetTimerLock1)
}

def resetTimerLock1() {
state.timerlock = true
LOGDEBUG( "Timer reset - Actions allowed again...")
}

// end lock actions ==================================

// end Actions ****************************************************************************




// Check volume levels ****************************************

def checkVolume(){
	if(doVolume == true){ 
def timecheck = fromTime2
if (timecheck != null){
def between2 = timeOfDayIsBetween(toDateTime(fromTime2), toDateTime(toTime2), new Date(), location.timeZone)
    if (between2) {
    
    state.volume = volume2
		
if(state.selection2 == "Speak A Message (Speech Synth)"){speaker.setVolume(state.volume)}		
if(state.selection2 == "Speak A Message (Music Player)"){speaker.setLevel(state.volume)}
    
   LOGDEBUG("Quiet Time = Yes - Setting Quiet time volume")
    
}
else if (!between2) {
state.volume = volume1
LOGDEBUG("Quiet Time = No - Setting Normal time volume")

	
	if(state.selection2 == "Speak A Message (Speech Synth)"){speaker.setVolume(state.volume)}	
if(state.selection2 == "Speak A Message (Music Player)"){speaker.setLevel(state.volume)}
 
	}
}
else if (timecheck == null){

state.volume = volume1
	
	if(state.selection2 == "Speak A Message (Speech Synth)"){speaker.setVolume(state.volume)}	
	if(state.selection2 == "Speak A Message (Music Player)"){ speaker.setLevel(state.volume)}

	}
  }
}


def checkAllow(){
    state.allAllow = false
    LOGDEBUG("Checking for any restrictions...")
    if(state.pauseApp == true){log.warn "Unable to continue - App paused"}
    if(state.pauseApp == false){
        LOGDEBUG("Continue - App NOT paused")
        state.noPause = true
		state.modeCheck = true
		state.presenceRestriction = true
		state.presenceRestriction1 = true
		state.dayCheck = true
		state.sunGoNow = true
		state.timeOK = true
		state.modes = modes
		state.fromTime = fromTime
		state.days = days
		state.sunriseSunset = sunriseSunset
		state.restrictPresenceSensor = restrictPresenceSensor
		state.restrictPresenceSensor1 = restrictPresenceSensor1
		state.timeYes = timeYes
		state.enableSwitchYes = enableSwitchYes
		state.modesYes = modesYes
		state.dayYes = dayYes
		state.sunrisesetYes = sunrisesetYes
		state.presenceYes = presenceYes
		
		if(state.enableSwitchYes == false){
		state.appgo1 = true
		state.appgo2 = true
		}
		if(state.modes != null && state.modesYes == true){modeCheck()}	
		if(state.fromTime !=null && state.timeYes == true){checkTime()}
		if(state.days!=null && state.dayYes == true){checkDay()}
		if(state.sunriseSunset !=null && state.sunrisesetYes == true){checkSun()}
		if(state.restrictPresenceSensor != null && state.presenceYes == true){checkPresence()}
        if(state.restrictPresenceSensor1 != null && state.presenceYes == true){checkPresence1()}
 
	if(state.modeCheck == false){
	LOGDEBUG("Not in correct 'mode' to continue")
	    }    
	if(state.presenceRestriction ==  false || state.presenceRestriction1 ==  false){
	LOGDEBUG( "Cannot continue - Presence failed")
	}
	if(state.appgo1 == false){
	LOGDEBUG("$enableSwitch1 is not in the correct position so cannot continue")
	}
	if(state.appgo2 == false){
	LOGDEBUG("$enableSwitch2 is not in the correct position so cannot continue")
	}
	if(state.appgo1 == true && state.appgo2 == true && state.dayCheck == true && state.presenceRestriction == true && state.presenceRestriction1 == true && state.modeCheck == true && state.timeOK == true && state.noPause == true && state.sunGoNow == true){
	state.allAllow = true 
 	  }
	else{
 	state.allAllow = false
	LOGWARN( "One or more restrictions apply - Unable to continue")
 	LOGDEBUG("state.appgo1 = $state.appgo1, state.appgo2 = $state.appgo2, state.dayCheck = $state.dayCheck, state.presenceRestriction = $state.presenceRestriction, state.presenceRestriction1 = $state.presenceRestriction1, state.modeCheck = $state.modeCheck, state.timeOK = $state.timeOK, state.noPause = $state.noPause, state.sunGoNow = $state.sunGoNow")
      }
   }

}

def checkSun(){
	LOGDEBUG("Checking Sunrise/Sunset restrictions...")
	if(!sunriseSunset){
        state.sunGoNow = true
        LOGDEBUG("No Sunrise/Sunset restrictions in place")	
	}
        if(sunriseSunset){
        if(sunriseSunset == "Sunset"){	
        if(state.astro == "Set"){
        state.sunGoNow = true
        LOGDEBUG("Sunset OK")
            } 
    	if(state.astro == "Rise"){
        state.sunGoNow = false
        LOGDEBUG("Sunset NOT OK")
            } 
        }
	if(sunriseSunset == "Sunrise"){	
        if(state.astro == "Rise"){
        state.sunGoNow = true
        LOGDEBUG("Sunrise OK")
            } 
    	if(state.astro == "Set"){
        state.sunGoNow = false
        LOGDEBUG("Sunrise NOT OK")
            } 
        }  
    } 
		return state.sunGoNow
}    

def astroCheck() {
    state.sunsetOffsetValue1 = sunsetOffsetValue
    state.sunriseOffsetValue1 = sunriseOffsetValue
    if(sunsetOffsetDir == "Before"){state.sunsetOffset1 = -state.sunsetOffsetValue1}
    if(sunsetOffsetDir == "after"){state.sunsetOffset1 = state.sunsetOffsetValue1}
    if(sunriseOffsetDir == "Before"){state.sunriseOffset1 = -state.sunriseOffsetValue1}
    if(sunriseOffsetDir == "after"){state.sunriseOffset1 = state.sunriseOffsetValue1}
	def both = getSunriseAndSunset(sunriseOffset: state.sunriseOffset1, sunsetOffset: state.sunsetOffset1)
	def now = new Date()
	def riseTime = both.sunrise
	def setTime = both.sunset
	LOGDEBUG("riseTime: $riseTime")
	LOGDEBUG("setTime: $setTime")
	unschedule("sunriseHandler")
	unschedule("sunsetHandler")
	if (riseTime.after(now)) {
	LOGDEBUG("scheduling sunrise handler for $riseTime")
	runOnce(riseTime, sunriseHandler)
		}
	if(setTime.after(now)) {
	LOGDEBUG("scheduling sunset handler for $setTime")
	runOnce(setTime, sunsetHandler)
		}
	LOGDEBUG("AstroCheck Complete")
}

def sunsetHandler(evt) {
	LOGDEBUG("Sun has set!")
	state.astro = "Set" 
}
def sunriseHandler(evt) {
	LOGDEBUG("Sun has risen!")
	state.astro = "Rise"
}

def modeCheck() {
    LOGDEBUG("Checking for any 'mode' restrictions...")
	def result = !modes || modes.contains(location.mode)
    LOGDEBUG("Mode = $result")
    state.modeCheck = result
    return state.modeCheck
 }



def checkTime(){
    LOGDEBUG("Checking for any time restrictions")
	def timecheckNow = fromTime
	if (timecheckNow != null){
    
def between = timeOfDayIsBetween(toDateTime(fromTime), toDateTime(toTime), new Date(), location.timeZone)
    if (between) {
    state.timeOK = true
   LOGDEBUG("Time is ok so can continue...")
    
}
	else if (!between) {
	state.timeOK = false
	LOGDEBUG("Time is NOT ok so cannot continue...")
	}
  }
	else if (timecheckNow == null){  
	state.timeOK = true
  	LOGDEBUG("Time restrictions have not been configured -  Continue...")
  }
}



def checkDay(){
    LOGDEBUG("Checking for any 'Day' restrictions")
	def daycheckNow = days
	if (daycheckNow != null){
 	def df = new java.text.SimpleDateFormat("EEEE")
    df.setTimeZone(location.timeZone)
    def day = df.format(new Date())
    def dayCheck1 = days.contains(day)
    if (dayCheck1) {
	state.dayCheck = true
	LOGDEBUG( "Day ok so can continue...")
 }       
 	else {
	LOGDEBUG( "Cannot run today!")
 	state.dayCheck = false
 	}
 }
if (daycheckNow == null){ 
	LOGDEBUG("Day restrictions have not been configured -  Continue...")
	state.dayCheck = true 
	} 
}

def restrictPresenceSensorHandler(evt){
	state.presencestatus1 = evt.value
	LOGDEBUG("state.presencestatus1 = $evt.value")
	checkPresence()
}



def checkPresence(){
	LOGDEBUG("Running checkPresence - restrictPresenceSensor = $restrictPresenceSensor")
	if(restrictPresenceSensor){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict = restrictPresenceAction
	if (state.presencestatus1 == "present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "not present" && actionPresenceRestrict == true){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}

	if (state.presencestatus1 == "not present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence ok")
	state.presenceRestriction = true
	}
	if (state.presencestatus1 == "present" && actionPresenceRestrict == false){
	LOGDEBUG("Presence not ok")
	state.presenceRestriction = false
	}
}
	else if(restrictPresenceSensor == null){
	state.presenceRestriction = true
	LOGDEBUG("Presence sensor restriction not used")
	}
}


def restrictPresence1SensorHandler(evt){
	state.presencestatus2 = evt.value
	LOGDEBUG("state.presencestatus2 = $evt.value")
	checkPresence1()
}


def checkPresence1(){
	LOGDEBUG("running checkPresence1 - restrictPresenceSensor1 = $restrictPresenceSensor1")
	if(restrictPresenceSensor1){
	LOGDEBUG("Presence = $state.presencestatus1")
	def actionPresenceRestrict1 = restrictPresenceAction1
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == true){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
	if (state.presencestatus2 == "not present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 ok - Continue..")
	state.presenceRestriction1 = true
	}
	if (state.presencestatus2 == "present" && actionPresenceRestrict1 == false){
	LOGDEBUG("Presence 2 not ok")
	state.presenceRestriction1 = false
	}
  }
	if(restrictPresenceSensor1 == null){
	state.presenceRestriction1 = true
	LOGDEBUG("Presence sensor 2 restriction not used - Continue..")
	}
}

def switchEnable1(evt){
	state.enableInput1 = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode1 == true && state.enableInput1 == 'off'){
	state.appgo1 = false
	LOGDEBUG("Cannot continue - App disabled by switch1")  
    }
	if(enableSwitchMode1 == true && state.enableInput1 == 'on'){
	state.appgo1 = true
	LOGDEBUG("Switch1 restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode1 == false && state.enableInput1 == 'off'){
	state.appgo1 = true
	LOGDEBUG("Switch1 restriction is OK.. Continue...")  
    }
	if(enableSwitchMode1 == false && state.enableInput1 == 'on'){
	state.appgo1 = false
	LOGDEBUG("Cannot continue - App disabled by switch1")  
    }    
	LOGDEBUG("Allow by switch1 is $state.appgo1")
}

def switchEnable2(evt){
	state.enableInput2 = evt.value
	LOGDEBUG("Switch changed to: $state.enableInput")  
    if(enableSwitchMode2 == true && state.enableInput2 == 'off'){
	state.appgo2 = false
	LOGDEBUG("Cannot continue - App disabled by switch2")  
    }
	if(enableSwitchMode2 == true && state.enableInput2 == 'on'){
	state.appgo2 = true
	LOGDEBUG("Switch2 restriction is OK.. Continue...") 
    }    
	if(enableSwitchMode2 == false && state.enableInput2 == 'off'){
	state.appgo2 = true
	LOGDEBUG("Switch2 restriction is OK.. Continue...")  
    }
	if(enableSwitchMode2 == false && state.enableInput2 == 'on'){
	state.appgo2 = false
	LOGDEBUG("Cannot continue - App disabled by switch2")  
    }    
	LOGDEBUG("Allow by switch2 is $state.appgo2")
}





def version(){
	setDefaults()
	pauseOrNot()
	logCheck()
	resetBtnName()
	schedule("0 0 9 ? * FRI *", updateCheck) //  Check for updates at 9am every Friday
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
//	unschedule()
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

def cobra(){
	log.warn "Previous schedule for old 'Cobra Update' found... Removing......"
	unschedule(cobra)
	log.info "Cleanup Complete!"
}


def setDefaults(){
    LOGDEBUG("Initialising defaults...")
    if(pause1 == null){pause1 = false}
    if(state.pauseApp == null){state.pauseApp = false}
    if(enableSwitch1 == null){
    LOGDEBUG("Enable switch1 is NOT used.. Continue..")
    state.appgo1 = true
	}
	if(enableSwitch2 == null){
    LOGDEBUG("Enable switch2 is NOT used.. Continue..")
    state.appgo2 = true	
    }
	state.restrictRun = false
	state.timer1 = true
	state.timerDoor = true
    state.timerlock = true
}




def setVersion(){
		state.version = "3.6.0"
     		state.InternalName = "PresenceCentralChild"
    		state.ExternalName = "Presence Central Child"
			state.preCheckMessage = "This app is designed to react to presence sensor(s)." 
			state.CobraAppCheck = "presencecentral.json"
}

