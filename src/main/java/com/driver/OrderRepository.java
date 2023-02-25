package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<Order>> deliveryPartnerOrderHashMap = new HashMap<>();

    HashSet<String> notAssigned = new HashSet<>();

    public void addOrder(Order order){
        String id = order.getId();
        notAssigned.add(id);
        orderHashMap.put(id,order);
    }

    public void addPartner(DeliveryPartner deliveryPartner){
        String id = deliveryPartner.getId();
        deliveryPartnerHashMap.put(id,deliveryPartner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        List<Order> orders = new ArrayList<>();
        Order order = orderHashMap.getOrDefault(orderId,null);
        orders.add(order);
        notAssigned.remove(orderId);
        deliveryPartnerOrderHashMap.put(partnerId, orders);
    }

    public Order getOrderById(String id){
        return orderHashMap.getOrDefault(id,null);
    }

    public DeliveryPartner getPartnerById(String id){
        return deliveryPartnerHashMap.getOrDefault(id,null);
    }

    public int getOrderCountByPartnerId(String partnerId){
        List<Order> orders = new ArrayList<>();
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            orders = deliveryPartnerOrderHashMap.get(partnerId);
        }
        return  orders.size();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        List<String> ordersName = new ArrayList<>();
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<Order> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (Order order : orders){
                ordersName.add(order.getId());
            }
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

        public Integer getCountOfUnassignedOrders(){
            return notAssigned.size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        Integer ordersLeft =0;
        int tm = Integer.parseInt(time);
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<Order> orders = new ArrayList<>();
            orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (Order order: orders){
                if (order.getDeliveryTime()>tm){
                    ordersLeft++;
                }
            }
        }
        return ordersLeft;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int deliverTm = 0;
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<Order> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (Order order: orders){
                int tm = order.getDeliveryTime();
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
            List<Order> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (Order order:orders){
                orders.remove(order);
                notAssigned.remove(order);
            }
            deliveryPartnerOrderHashMap.remove(partnerId);
        }
        if (deliveryPartnerHashMap.containsKey(partnerId)){
            deliveryPartnerHashMap.remove(partnerId);
        }
    }

    public void deleteOrderById(String orderId){
        for (Map.Entry<String, List<Order>> map: deliveryPartnerOrderHashMap.entrySet()){
            List<Order> orders = map.getValue();
            for (Order order:orders){
                if (order.getId().equals(orderId)){
                    orders.remove(order);
                    notAssigned.remove(order.getId());
                }
            }
        }
        if (orderHashMap.containsKey(orderId)){
            orderHashMap.remove(orderId);
        }
    }
}
