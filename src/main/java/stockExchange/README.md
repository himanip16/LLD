You are asked to design a simplified stock exchange system that supports trading of stocks by two types of traders: individual traders and institutional traders.

The system should allow users to place buy and sell orders for stocks and handle order notifications based on trader type.

Requirements
Traders

There are two types of traders:

Individual Trader
Institutional Trader
Both types of traders can place buy and sell orders for stocks.

Institutional traders have an extra capability compared to individual traders.

Stocks

The system supports multiple stocks identified by a unique symbol.
Each stock can have multiple buy and sell orders placed by traders.
Orders

An order includes:

Stock symbol
Order type (buy or sell)
Quantity
Price
Trader who placed the order
Orders are submitted to the stock exchange for processing.

Subscriptions and Notifications

Institutional traders can subscribe to one or more stock symbols.
When any buy or sell order is placed for a subscribed stock, subscribed institutional traders should receive a real time notification.
Individual traders do not receive such notifications.
System Responsibilities

Manage registration of traders.
Allow institutional traders to subscribe or unsubscribe from stocks.
Accept buy and sell orders from traders.
Notify subscribed institutional traders when an order is placed for a stock they follow.
Ensure notifications are sent only for relevant stocks and only to eligible traders.
Constraints

Notifications should be sent immediately after an order is placed.
Multiple institutional traders can subscribe to the same stock.
A trader should not receive notifications for stocks they have not subscribed to.
Example Scenario
Stock symbols available: ABC, XYZ
Institutional Trader T1 subscribes to ABC
Individual Trader T2 places a buy order for ABC
Institutional Trader T1 receives a notification about the order
Individual Trader T3 places a sell order for XYZ
No notification is sent to T1
The focus of the interview is to design clean class structures, relationships between traders, stocks, orders, and the notification mechanism while keeping the system easy to extend.