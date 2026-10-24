<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>База данных выигрышных лотерейных билетов</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/lottery-styles.css">
</head>
<body>
    <div class="wrap">

        <header class="top">
            <h1>Выигрышные билеты</h1>
            <p>Тиражи, номера билетов и размеры выигрышей</p>
        </header>

        <c:if test="${not empty error}">
            <div class="alert" role="alert"><c:out value="${error}"/></div>
        </c:if>

        <div class="grid-2">
            <section class="panel">
                <h2>Найти тираж</h2>
                <form class="inline-form" method="get" action="lottery">
                    <input type="hidden" name="action" value="specificTicket">
                    <label>Номер тиража
                        <input type="number" name="ticketId" min="1" required>
                    </label>
                    <button class="btn" type="submit">Найти</button>
                </form>
            </section>

            <section class="panel">
                <h2>Добавить билет</h2>
                <form class="stack" method="post" action="lottery" accept-charset="UTF-8">
                    <label>Номер билета
                        <input type="number" name="ticketNumber" required>
                    </label>
                    <label>Сумма выигрыша, ₽
                        <input type="number" name="prizeAmount" min="0" required>
                    </label>
                    <label>Дата и время розыгрыша
                        <input type="datetime-local" step="1" name="drawDate">
                    </label>
                    <p class="hint">Если не указать, возьмётся текущее время.</p>
                    <button class="btn" type="submit">Добавить</button>
                </form>
            </section>
        </div>

        <c:choose>

            <%-- Одиночный билет: таблица не показывается --%>
            <c:when test="${param.action == 'specificTicket'}">
                <p class="back"><a class="btn ghost small" href="lottery">← Ко всем билетам</a></p>
                <c:choose>
                    <c:when test="${not empty specificTicket}">
                        <c:set var="rawDt" value="${fn:substring(specificTicket.dateTime, 0, 19)}"/>
                        <c:set var="fullDt" value="${rawDt}${fn:length(rawDt) == 16 ? ':00' : ''}"/>
                        <article class="ticket">
                            <div class="stub">
                                <small>Тираж</small>
                                <strong><c:out value="${specificTicket.drawNumber}"/></strong>
                            </div>
                            <div class="body">
                                <div>Билет № <strong><c:out value="${specificTicket.ticketNumber}"/></strong></div>
                                <div class="big"><fmt:formatNumber value="${specificTicket.prizeAmount}" pattern="#,##0"/> ₽</div>
                                <div>Розыгрыш: <c:out value="${fn:replace(fullDt, 'T', ' ')}"/></div>
                            </div>
                        </article>
                    </c:when>
                    <c:when test="${empty error}">
                        <div class="panel empty">Тираж с таким номером не найден.</div>
                    </c:when>
                </c:choose>
            </c:when>

            <%-- Все билеты --%>
            <c:otherwise>
                <section class="panel">
                    <div class="toolbar">
                        <h2>Все билеты</h2>
                        <a class="btn ghost small" href="lottery">Обновить список</a>
                    </div>

                    <div class="table-scroll">
                        <table>
                            <thead>
                                <tr>
                                    <th class="num">Тираж</th>
                                    <th class="num">Билет</th>
                                    <th class="num">Выигрыш</th>
                                    <th>Дата розыгрыша</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${ticketsArray}">
                                    <c:set var="rawDt" value="${fn:substring(t.dateTime, 0, 19)}"/>
                                    <c:set var="fullDt" value="${rawDt}${fn:length(rawDt) == 16 ? ':00' : ''}"/>
                                    <tr>
                                        <td class="num"><c:out value="${t.drawNumber}"/></td>
                                        <td class="num"><c:out value="${t.ticketNumber}"/></td>
                                        <td class="num prize"><fmt:formatNumber value="${t.prizeAmount}" pattern="#,##0"/> ₽</td>
                                        <td><c:out value="${fn:replace(fullDt, 'T', ' ')}"/></td>
                                        <td class="actions">
                                            <button class="btn ghost small" type="button"
                                                    data-draw="${t.drawNumber}"
                                                    data-ticket="${t.ticketNumber}"
                                                    data-prize="${t.prizeAmount}"
                                                    data-date="${fullDt}"
                                                    onclick="openEdit(this)">Изменить</button>
                                            <button class="btn danger small" type="button"
                                                    onclick="removeTicket(${t.drawNumber})">Удалить</button>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty ticketsArray}">
                                    <tr><td colspan="5" class="empty">Билетов пока нет. Добавьте первый выше.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </section>
            </c:otherwise>

        </c:choose>
    </div>

    <dialog id="editDialog">
        <h2>Изменить тираж <span id="editDrawLabel"></span></h2>
        <form class="stack" id="editForm" onsubmit="return saveEdit(event)">
            <input type="hidden" name="drawNumber" id="editDraw">
            <label>Номер билета
                <input type="number" name="ticketNumber" id="editTicket">
            </label>
            <label>Сумма выигрыша, ₽
                <input type="number" name="prizeAmount" id="editPrize" min="0">
            </label>
            <label>Дата и время розыгрыша
                <input type="datetime-local" step="1" name="drawDate" id="editDate">
            </label>
            <p class="hint">Пустое поле оставит старое значение.</p>
            <div class="row">
                <button class="btn ghost" type="button" onclick="editDialog.close()">Отмена</button>
                <button class="btn" type="submit">Сохранить</button>
            </div>
        </form>
    </dialog>

    <script>
        const editDialog = document.getElementById('editDialog');

        // PUT и DELETE: параметры уходят в query string, потому что Tomcat не разбирает
        // тело form-urlencoded для этих методов. redirect:'manual' — чтобы не повторять
        // PUT/DELETE на редиректе; после ответа просто перезагружаем страницу.
        function send(method, params) {
            const url = 'lottery?' + new URLSearchParams(params).toString();
            fetch(url, { method: method, redirect: 'manual' })
                .finally(() => { location.href = 'lottery'; });
        }

        function openEdit(btn) {
            document.getElementById('editDraw').value = btn.dataset.draw;
            document.getElementById('editDrawLabel').textContent = '№ ' + btn.dataset.draw;
            document.getElementById('editTicket').value = btn.dataset.ticket;
            document.getElementById('editPrize').value = btn.dataset.prize;
            document.getElementById('editDate').value = btn.dataset.date;
            editDialog.showModal();
        }

        function saveEdit(e) {
            e.preventDefault();
            const f = e.target;
            send('PUT', {
                drawNumber: f.drawNumber.value,
                ticketNumber: f.ticketNumber.value,
                prizeAmount: f.prizeAmount.value,
                drawDate: f.drawDate.value
            });
            return false;
        }

        function removeTicket(draw) {
            if (confirm('Удалить тираж № ' + draw + '?')) {
                send('DELETE', { drawNumber: draw });
            }
        }
    </script>
</body>
</html>
