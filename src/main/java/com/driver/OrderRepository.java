package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    Integer unassigned = null;
    HashMap<String, Order> orderHashMap = new HashMap<>();
    HashMap<String,DeliveryPartner> deliveryPartnerHashMap = new HashMap<>();

    HashMap<String, List<Order>> deliveryPartnerOrderHashMap = new HashMap<>();

    public void addOrder(Order order){
        String id = order.getId();
        if (unassigned==null){
            unassigned=1;
        }
        else {
            unassigned++;
        }
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
        unassigned--;
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
        return unassigned;
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
        return Integer.toString(deliverTm);
    }
    public void deletePartnerById(String partnerId){
        if (deliveryPartnerOrderHashMap.containsKey(partnerId)){
            List<Order> orders = deliveryPartnerOrderHashMap.get(partnerId);
            for (Order order:orders){
                orders.remove(order);
                unassigned++;
            }
            deliveryPartnerOrderHashMap.remove(partnerId);
        }
        deliveryPartnerHashMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId){
        for (Map.Entry<String, List<Order>> map: deliveryPartnerOrderHashMap.entrySet()){
            List<Order> orders = map.getValue();
            for (Order order:orders){
                if (order.getId().equals(orderId)){
                    unassigned++;
                    orders.remove(order);
                }
            }
        }
        unassigned--;
        orderHashMap.remove(orderId);
    }
}
