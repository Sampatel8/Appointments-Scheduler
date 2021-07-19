@RestResource(urlMapping='/login/*')
global class GetUser {
    //login
    @HttpPost
    global static Contact getUserData(String userId){
        return [SELECT Id,FirstName,Is_PC__c,User_Id__c,LastName FROM Contact WHERE User_Id__c = :userId];
    }

}