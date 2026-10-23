<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Сортировка массива</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <h1>Сортировка массива</h1>

    <form action="${pageContext.request.contextPath}/servlet" method="POST">
        <input type="text" name="array" id="array" placeholder="Введите массив через запятую..." required autocomplete="off">
        <button type="submit">Отсортировать</button>
    </form>

    <c:if test="${not empty error}">
        <div class="error-box">
            ⚠️ Ошибка: ${error}
        </div>
    </c:if>

    <c:if test="${not empty initialArray}">
        <h3>Исходный массив:</h3>
        <p>
            <c:forEach var="number" items="${initialArray}" varStatus="status">
                <c:out value="${number}"/>${status.last ? '' : ', '}
            </c:forEach>
        </p>
        <br>
        <h3>Результаты сортировки массива:</h3>
        <p>
            <c:forEach var="number" items="${sortedArray}" varStatus="status">
                <c:out value="${number}"/>${status.last ? '' : ', '}
            </c:forEach>
        </p>
    </c:if>
</body>
</html>
