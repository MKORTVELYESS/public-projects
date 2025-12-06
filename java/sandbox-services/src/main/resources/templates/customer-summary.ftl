<#macro customerSummary customer>
Customer: ${customer.name?upper_case}
Email: ${customer.email?lower_case}
Age: ${customer.age}
VIP: <#if customer.vip?? && customer.vip == true>YES<#else>NO</#if>
Orders:
<#list customer.orders as order>
    - Order ID: ${order.id}
      Total: ${order.total?string.currency}
      Shipped: <#if order.shipped?? && order.shipped>Yes<#else>No</#if>
      Items:
      <#list order.items as item>
          * ${item.name} x ${item.qty} (${item.price?string.currency} each)
      </#list>
      <#assign total = order.total?number>
      <#assign discount = (order.discount?default(0))?number>
      Discounted Total: ${(total * (1 - discount / 100))?string.currency}
</#list>
</#macro>