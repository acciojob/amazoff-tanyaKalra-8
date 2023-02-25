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

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        Integer ordersLeft =0;
        int tm = Integer.parseInt(time);
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<String> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (String order: orders){
                Order order1 = orderHashMap.get(order);
                if (order1.getDeliveryTime()>tm){
                    ordersLeft++;
                }
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int deliverTm = 0;
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<String> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (String order: orders){
                Order order1 = orderHashMap.get(order);
                int tm = order1.getDeliveryTime();
                if (tm> deliverTm){
                    deliverTm = tm;
                }
            }
        }
        int hours= deliverTm/60;
        int min= deliverTm%60;
        String strhours = Integer.toString(hours);
        if(strhours.length()==1){
            strhours = "0"+strhours;
        }

        String minutes = Integer.toString(min);

        if(minutes.length()==1){
            minutes = "0" + minutes;
        }
        return strhours + ":" + minutes;
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
