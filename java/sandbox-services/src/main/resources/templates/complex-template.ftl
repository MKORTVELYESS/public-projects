<#include "calculate-tax.ftl">
<#include "customer-summary.ftl">
<#include "include-header.ftl">
<#include "reverse-string.ftl">

<#list customers as customer>
<@customerSummary customer=customer />
--------------------------------------
</#list>

<#if specialEvent??>
Special Event: ${specialEvent.name}
<#elseif promotion??>
Promotion: ${promotion.description}
<#else>
No current events
</#if>

Total Tax: ${calculateTax(1234.56, 7.5)?string.currency}
Random String: ${reverseString("===freemarker-test===")}


