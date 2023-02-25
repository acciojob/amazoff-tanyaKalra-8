package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<String>> deliveryPartnerOrderHashMap = new HashMap<>();

    HashMap<String, String> Assigned = new HashMap<>();

    public void addOrder(Order order){
        String id = order.getId();
        orderHashMap.put(id,order);
    }

    public void addPartner(String id){
        DeliveryPartner deliveryPartner = new DeliveryPartner(id);
        deliveryPartnerHashMap.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        List<String> list = deliveryPartnerOrderHashMap.getOrDefault(partnerId,new ArrayList<>());
        list.add(orderId);
        deliveryPartnerOrderHashMap.put(partnerId,list);
        Assigned.put(orderId,partnerId);
        DeliveryPartner deliveryPartner = deliveryPartnerHashMap.get(partnerId);
        deliveryPartner.setNumberOfOrders(list.size());
    }

    public Order getOrderById(String id){
        for(String s:orderHashMap.keySet())
        {
            if(s.equals(id))
            {
                return orderHashMap.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String id){
        if (deliveryPartnerHashMap.containsKey(id)){
            return deliveryPartnerHashMap.get(id);
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId){
        return deliveryPartnerOrderHashMap.getOrDefault(partnerId,new ArrayList<>()).size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> ordersName = new ArrayList<>();
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            ordersName = deliveryPartnerOrderHashMap.get(partnerId);
        }
        return ordersName;
    }

    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        for (Map.Entry<String,Order> map: orderHashMap.entrySet()){
            orders.add(map.getKey());
        }
        return orders;
    }

        public int getCountOfUnassignedOrders(){
            int count= orderHashMap.size() - Assigned.size();
            return count;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        int countOfOrders = 0;
        List<String> list = deliveryPartnerOrderHashMap.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderHashMap.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        String time = "";
        List<String> list = deliveryPartnerOrderHashMap.get(partnerId);
        int deliveryTime = 0;
        for (String s : list) {
            Order order = orderHashMap.get(s);
            deliveryTime = Math.max(deliveryTime, order.getDeliveryTime());
        }
        int hour = deliveryTime / 60;
        String sHour = "";
        if (hour < 10) {
            sHour = "0" + String.valueOf(hour);
        } else {
            sHour = String.valueOf(hour);
        }
        int min = deliveryTime % 60;
        String sMin = "";
        if (min < 10) {
            sMin = "0" + String.valueOf(min);
        } else {
            sMin = String.valueOf(min);
        }
        time = sHour + ":" + sMin;
        return time;
    }

    public void deletePartnerById(String partnerId){
        deliveryPartnerHashMap.remove(partnerId);
        List<String> list = deliveryPartnerOrderHashMap.getOrDefault(partnerId, new ArrayList<>());
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            Assigned.remove(s);
        }
        deliveryPartnerOrderHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        orderHashMap.remove(orderId);
        String partnerId = Assigned.get(orderId);
        Assigned.remove(orderId);
        List<String> list = deliveryPartnerOrderHashMap.get(partnerId);
        ListIterator<String> itr = list.listIterator();
        while (itr.hasNext()) {
            String s = itr.next();
            if (s.equals(orderId)) {
                itr.remove();
            }
        }
        deliveryPartnerOrderHashMap.put(partnerId, list);
    }
}
