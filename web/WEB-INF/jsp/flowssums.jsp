            Flows sums: <!--${flowsSums}<br/--> 
            
            <c:forEach var="flow" items="${flowsSums}">
                <h3>Device ip: ${flow.key}</h3>
                <c:forEach var="yearFlow" items="${flow.value}">
                    <h4>Year: ${yearFlow.key}</h4>
                    <c:forEach var="monthFlow" items="${yearFlow.value}">
                        <h5>Month: ${monthFlow.key+1}</h5>
                        <c:forEach var="dayFlow" items="${monthFlow.value}">
                            <b>Day: ${dayFlow.key}</b><br/>
                            <c:forEach var="hourFlow" items="${dayFlow.value}">
                                ${hourFlow.key} = ${hourFlow.value} Mb<br/>
                            </c:forEach>
                        </c:forEach>
                    </c:forEach>
                </c:forEach>              
            </c:forEach>
                                
