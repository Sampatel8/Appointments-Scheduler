public class CreateTimeSlots {
    public static void create(List<Availability__c> avails){
        Map<String,Availability__c> availMap = new Map<String,Availability__c>();
        for(Availability__c avail : avails){
            availMap.put(avail.Day_Of_Week__c,avail);    
        }
        
        List<Slot__c> slots = new List<Slot__c>();
        for(Availability__c avail : avails){
            Time stTime = avail.Start_Time__c;
            System.debug(avail.Start_Time__c);
            System.debug(avail.End_Time__c);
            Date nowDate = Date.today();
            for(Integer i = 0;i<7;i++){
                DateTime myDateTime = (DateTime) nowDate;
                String dayOfWeek = myDateTime.format('EEEE');
                if(availMap.get(dayOfWeek) == avail){
                    while(stTime < avail.End_Time__c){
                        Slot__c slot = new Slot__c();
                        slot.Start_Time__c = stTime;
                        stTime = stTime.addMinutes(60);
                        slot.End_Time__c = stTime.addMinutes(-1);
                        slot.Day_Of_Week__c = avail.Day_Of_Week__c;
                        slot.Date__c = nowDate.addDays(-1);
                        slot.PC_Id__c = avail.PC_Id__c;
                        slots.add(slot);
                    }
                }
                nowDate = nowDate.addDays(1);
            }
        }
        insert slots;
    }
}