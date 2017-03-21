<!-- See https://github.com/twitter/typeahead.js/issues/193 -->
<input type="text" 
       id="${id}"
       value="${value?:''}" 
       name="${name}" 
       data-domain="${baseClass}" 
       class="simpleReferenceTypedown"
       <g:if test="${filter1}">data-filter1="${filter1}"</g:if> />
