<#function reverseString s>
    <#assign result = "">
    <#list 0..(s?length - 1) as i>
        <#assign result = s[s?length - 1 - i] + result>
    </#list>
    <#return result>
</#function>