@RestResource(urlMapping='/availability/*')
global class InsertAvailability {
    //update availability
	@HttpPost
    global static void insertAvails(List<Availability__c> avails){
        if(avails!=null){
            insert avails;
            CreateTimeSlots.create(avails);
        }else System.debug('avils is null');
    }
    
    @HttpGet
    global static List<Availability__c> getAvails(){
        String contactId = RestContext.request.params.get('contactId');
        if(contactId!=null){
            return [SELECT Day_Of_Week__c,Start_Time__c,End_Time__c,PC_Id__c FROM Availability__c WHERE PC_Id__c = :contactId];
        }
        return null;
    }
}