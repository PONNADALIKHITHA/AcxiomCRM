from django.urls import path
from .views import dashboard, customers, leads, opportunities, followups
urlpatterns = [
    path("", dashboard, name="dashboard"),
    path("customers/", customers, name="customers"),
    path("leads/", leads, name="leads"),
    path("opportunities/", opportunities, name="opportunities"),
    path("followups/", followups, name="followups"),
]
