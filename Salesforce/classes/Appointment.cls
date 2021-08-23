@RestResource(urlMapping='/appointment/*')
global class Appointment {
    //insert appointment
	@HttpPost
    global static Appointment__c insertAppointment(Appointment__c appointment){
        //System.debug('appintment'+appointment);
        if(appointment!= null){
            Id slotId = appointment.Slot__c;
            upsert appointment;
            Notification__c noti = new Notification__c();
            noti.Appointment__c = appointment.Id;
            noti.Tag__c = appointment.Status__c == null?'Pending':appointment.Status__c;
            insert noti;
            if(appointment.Slot__c!=null){
                Slot__c slot = new Slot__c(Id=appointment.Slot__c,Is_Assigned__c = (appointment.Status__c !=null && appointment.Status__c.equals('Rejected'))?false:true);
            update slot;                              
           }
        }
        return appointment;
    }
    
    //all Appointments
    @HttpGet
    global static List<Appointment__c> getAll(){
        String contactId = RestContext.request.params.get('contactId');
        String status = RestContext.request.params.get('status');
        System.debug(contactId);
        System.debug(status);
        if(status == null){
            return [SELECT Id,Status__c,Slot__r.Start_Time__c,Slot__r.End_Time__c,Slot__r.Date__c,Name,Description__c,From_Contact__r.Id,To_Contact__r.Id,To_Contact__r.FirstName,To_Contact__r.LastName FROM Appointment__c WHERE (To_Contact__c = :contactId OR From_Contact__c = :contactId)];
        }
        return [SELECT Id,Status__c,Slot__r.Start_Time__c,Slot__r.End_Time__c,Slot__r.Date__c,Name,Description__c,From_Contact__r.Id,To_Contact__r.Id,To_Contact__r.FirstName,To_Contact__r.LastName FROM Appointment__c WHERE From_Contact__c = :contactId AND Status__c = :status];
    }
    
   
}