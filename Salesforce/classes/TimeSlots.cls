@RestResource(urlMapping='/slots/*')
global class TimeSlots {
    //getting timeslots
	@HttpPost
    global static List<Slot__c> getUserData(String contactId,String appDate){
        Date dt = Date.parse(appDate);
        return [SELECT Id,Start_Time__c,End_Time__c FROM Slot__c WHERE PC_Id__c = :contactId AND Date__c =:dt AND is_Assigned__c = false];
    }
}