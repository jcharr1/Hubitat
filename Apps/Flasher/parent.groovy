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
 * This agreement can be found on-line at: http://hubitat.uk/Software_License_Agreement.txt
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
 *  Last Update: 10/06/2019
 *
 *  Changes:
 *
 *
 *  V2.0.0 - Removed default connection to the Cobra Apps container (Line 52)
 *           Update check is now randomised
 *           This software is released under a new license agreement.
 *
 *  V1.3.0 - Added disable apps code
 *  V1.2.0 - Moved update checks to parent
 *  V1.1.0 - Added revised updated checking
 *  V1.0.0 - POC
 *
 */



definition(
    name: "Flasher",
    namespace: "Cobra",
    author: "Andrew Parker",
    description: "Parent App for Flasher ChildApps ",
    
//     parent: "Cobra:Cobra Apps",  // ******** Remove 'Comment' (//) if using the 'Cobra Apps' container  ***************
    
    category: "Convenience",
    iconUrl: "",
    iconX2Url: "",
    iconX3Url: ""
    )







preferences {page name: "mainPage", title: "", install: true, uninstall: true}
def installed() {initialize()}
def updated() {initialize()}
def initialize() {
    unsubscribe()
    version()
    log.debug "Initialised with settings: ${settings}"
    log.info "There are ${childApps.size()} child apps"
    childApps.each {child ->
    log.info "Child app: ${child.label}"
    }    
}
def mainPage() {
    dynamicPage(name: "mainPage") {  
	setVersion()
	installCheck()
	if(state.appInstalled == 'COMPLETE'){
	display()
	section (){app(name: "flasherApp", appName: "Flasher Child", namespace: "Cobra", title: "<b>Add a new automation</b>", multiple: true)}	
	displayDisable()
	}
  }
}



def version(){
    resetBtnName()
    updateCheck()  
    checkButtons()
	def random = new Random()
    Integer randomHour = random.nextInt(18-10) + 10
    Integer randomDayOfWeek = random.nextInt(7-1) + 1 // 1 to 7
    schedule("0 0 " + randomHour + " ? * " + randomDayOfWeek, updateCheck) 
   
}


def installCheck(){         
	state.appInstalled = app.getInstallationState() 
	if(state.appInstalled != 'COMPLETE'){
	section{paragraph "Please hit 'Done' to install this app<br><br>$state.agreementNotice"}
	
	  }
	else{
 //      log.info "Parent Installed OK"  
    }
	}

def display(){
	if(state.status){section(){paragraph "<img src='http://update.hubitat.uk/icons/cobra3.png''</img> Version: $state.version <br><font face='Lucida Handwriting'>$state.Copyright </font>"}}
	if(state.status != "<b>** This app is no longer supported by $state.author  **</b>"){section(){input "updateBtn", "button", title: "$state.btnName"}}
	if(state.status != "Current"){
		section(){paragraph "<hr><b>Updated: </b><i>$state.Comment</i><br><br><i>Changes in version $state.newver</i><br>$state.UpdateInfo<hr><b>Update URL: </b><font color = 'red'> $state.updateURI</font><hr>"}
		}
		section(){
		input "updateNotification", "bool", title: "Send a 'Pushover' message when an update is available for either the parent or the child app", required: true, defaultValue: false, submitOnChange: true 
		if(updateNotification == true){ input "speakerUpdate", "capability.speechSynthesis", title: "PushOver Device", required: true, multiple: true}
		}
	
}

def displayDisable(){
	if(app.label){
	section("<hr>"){
		input "disableAll1", "bool", title: "Disable <b>all</b> <i>'${app.label}'</i> child apps", required: true, defaultValue: false, submitOnChange: true
		state.allDisabled1 = disableAll1
		stopAll()
	}
	section("<hr>"){}
	}
	else{
	section("<hr>"){
		input "disableAll1", "bool", title: "Disable <b><i>ALL</i></b> child apps ", required: true, defaultValue: false, submitOnChange: true
		state.allDisabled1 = disableAll1
		stopAll()
	}
	section("<hr>"){}
	}
	
}




def stopAll(){
	
	if(state.allDisabled1 == true) {
	log.debug "state.allDisabled1 = TRUE"
	state.msg2 = "Disabled by parent"
	childApps.each { child ->
	child.stopAllChildren(state.allDisabled1, state.msg2)
	log.warn "Disabling ChildApp: $child.label"
	}
	}	
	
	if(state.allDisabled1 == false){
	log.debug "state.allDisabled1 = FALSE"
	state.msg3 = "Enabled by parent"
	childApps.each { child ->
	child.stopAllChildren(state.allDisabled1, state.msg3)	
	log.trace "Enabling ChildApp: $child.label "
	}
	}
}

/**
def stopAllParent(stopNowCobra, msgCobra){
	state.allDisabled1 = stopNowCobra
	def msgNowCobra = msgCobra
	log.info " Message from Cobra Apps -  Disable = $stopNowCobra"
	childApps.each { child ->
	child.stopAllChildren(state.allDisabled1, msgNowCobra)
	//	if(stopNowCobra == true){log.warn "Disabling ChildApp: $child.label"}
	//	if(stopNowCobra == false){log.trace "Enabling ChildApp: $child.label "}
		
		
		
	}
}	

*/


def checkButtons(){
//    log.debug "Running checkButtons"
    appButtonHandler("updateBtn")
}


def appButtonHandler(btn){
	state.btnCall = btn
	if(state.btnCall == "updateBtn"){
        log.info "Checking for updates now..."
        updateCheck()
        pause(3000)
	state.btnName = state.newBtn
        runIn(2, resetBtnName)
    }
    
}  
 
def resetBtnName(){
//    log.info "Resetting Button"	
	if(state.status != "Current"){
	state.btnName = state.newBtn
	    }
	else{
 	state.btnName = "Check For Update" 
	}
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
            pushOverUpdate(state.updateMsg)
       		} 
		else if(currentVer > newVer){
        	state.status = "You are using a BETA ($state.version) - Release Version: $newVerRaw"
        	log.warn "** <b>$state.status</b>) **"
        	state.UpdateInfo = "N/A"
		}	
		else{ 
      		state.status = "Current"
       		log.info("You are using the current version of this app")
       		}
      					}
        	} 
        catch (e) {
        	log.error "Something went wrong: CHECK THE JSON FILE AND IT'S URI -  $e"
    		}
    if(state.status != "Current"){
		state.newBtn = state.status
        
    }
    else{
        state.newBtn = "No Update Available"
    }
        
        
}

def childUpdate(set, msg){
	if(state.msgDone == false){
	state.childUpdate = set.value
	state.upMsg = msg.toString()
	if(state.childUpdate == true){
	pushOverUpdate(state.upMsg)	
	state.msgDone = true	
			}	
		}
	else{
//		log.info "Message already sent - Not able to send again today"
	    }		
}
def resetMsg(){state.msgDone = false}
def pushOverUpdate(inMsg){
    if(updateNotification == true){  
    newMessage = inMsg
   log.debug"PushOver Message = $newMessage "  
    state.msg1 = '[L]' + newMessage
    speakerUpdate.speak(state.msg1)
    }
}


def setVersion(){
		state.version = "2.0.0"	 
		state.InternalName = "FlasherParent" 
    	state.CobraAppCheck = "flasher.json"
		state.ExternalName = " Flasher Parent"
		state.agreementNotice = "By downloading, installing, and/or executing this software you hereby agree to the terms and conditions set forth in the Software license agreement.<br>This agreement can be found on-line at: http://hubitat.uk/Software_License_Agreement.txt"
    	
}











