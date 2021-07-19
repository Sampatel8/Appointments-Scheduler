@RestResource(urlMapping='/appointment/*')
global class Appointment {
    //insert appointment
	@HttpPost
    global static Appointment__c insertAppointment(Appointment__c appointment){
        if(appointment!= null){
            insert appointment;
            Slot__c slot = new Slot__c(Id=appointment.Slot__c,Is_Assigned__c = true);
            update slot;
        }
        return appointment;
    }
    
    //all Appointments
    @HttpGet
    global static List<Appointment__c> getAll(){
        String contactId = RestContext.request.params.get('contactId');
        String status = RestContext.request.params.get('status');
        if(status == null){
            return [SELECT Id,Status__c,Slot__r.Start_Time__c,Slot__r.End_Time__c,Slot__r.Date__c,Name,Description__c FROM Appointment__c WHERE From_Contact__c = :contactId];
        }
        return [SELECT Id,Status__c,Slot__r.Start_Time__c,Slot__r.End_Time__c,Slot__r.Date__c,Name,Description__c FROM Appointment__c WHERE From_Contact__c = :contactId AND Status__c = :status];
    }
}