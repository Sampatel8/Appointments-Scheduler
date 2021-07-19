@RestResource(urlMapping='/getContacts/*')
global class GetContacts {
    //spiner contacts
    @HttpGet
    global static List<Contact> getAll(){
        return [SELECT Id,FirstName,LastName FROM Contact LIMIT 50];
    }
    
    //register
    @HttpPost
    global static Contact insertContact(Contact contact){
        if(contact!=null){
            List<Permanent_Code__mdt> code = [SELECT Id,Is_PC__c FROM Permanent_Code__mdt WHERE Label = :contact.Department];
            if(!code.isEmpty()){
                contact.Is_PC__c = code[0].Is_PC__c;
                upsert contact User_Id__c;
                return contact;
            }
            
        }
        return null;
    }
}