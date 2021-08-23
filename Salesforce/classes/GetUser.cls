@RestResource(urlMapping='/login/*')
global class GetUser {
    //login
    @HttpPost
    global static Contact getUserData(String userId){
        return [SELECT Id,FirstName,Is_PC__c,Gender__c,Birthdate,User_Id__c,LastName,Department,Email FROM Contact WHERE User_Id__c = :userId];
    }

}