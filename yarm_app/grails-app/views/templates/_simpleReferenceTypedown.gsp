<select id="${id}"
        value="${value?:''}" 
        name="${name}" 
        style="${style?:''}" 
        data-domain="${baseClass}" 
        data-lookupurl="<g:createLink controller='ajaxSupport' action='lookup'/>"
        class="simpleReferenceTypedown typeahead ${cssCls}"
        <g:if test="${filter1}">data-filter1="${filter1}"</g:if> >
</select>
