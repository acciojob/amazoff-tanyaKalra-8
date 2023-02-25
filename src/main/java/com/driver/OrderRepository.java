package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<String>> deliveryPartnerOrderHashMap = new HashMap<>();

    HashSet<String> Assigned = new HashSet<>();

    public void addOrder(Order order){
        String id = order.getId();
        orderHashMap.put(id,order);
    }

    public void addPartner(DeliveryPartner deliveryPartner){
        String id = deliveryPartner.getId();
        deliveryPartnerHashMap.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        if(orderHashMap.containsKey(orderId) && deliveryPartnerHashMap.containsKey(partnerId))
        {
            Assigned.add(orderId);
            orderHashMap.put(orderId,orderHashMap.get(orderId));
            deliveryPartnerHashMap.put(partnerId,deliveryPartnerHashMap.get(partnerId));
            List<String> curr =new ArrayList<>();
            if(deliveryPartnerOrderHashMap.containsKey(partnerId))
            {
                curr=deliveryPartnerOrderHashMap.get(partnerId);
            }
            curr.add(orderId);
            deliveryPartnerOrderHashMap.put(partnerId,curr);
        }
    }

    public Order getOrderById(String id){
        for(String s:orderHashMap.keySet())
        {
            if(s.equals(id))
            {
                return orderHashMap.get(id);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String id){
        for(String s:deliveryPartnerHashMap.keySet())
        {
            if(s.equals(id))
            {
                return deliveryPartnerHashMap.get(id);
            }
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId){
        int count = deliveryPartnerOrderHashMap.getOrDefault(partnerId,new ArrayList<>()).size();
        return count;
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
            List<String> orders;
            orders = deliveryPartnerOrderHashMap.get(partnerId);
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
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<String> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (String order:orders){
                orders.remove(order);
                Assigned.remove(order);
            }
            deliveryPartnerOrderHashMap.remove(partnerId);
        }
        deliveryPartnerHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        for (Map.Entry<String, List<String>> map: deliveryPartnerOrderHashMap.entrySet()){
            List<String> orders = map.getValue();
            for (String order:orders){
                if (order.equals(orderId)){
                    orders.remove(order);
                    Assigned.remove(order);
                }
            }
        }
        orderHashMap.remove(orderId);
    }
}
