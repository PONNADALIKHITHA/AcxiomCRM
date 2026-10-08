import requests
from django.shortcuts import render
from django.http import HttpResponse

API = "http://localhost:8080"

def api_get(path):
    try:
        r=requests.post(API+"/api/auth/login",json={"username":"admin","password":"Admin@123"},timeout=3)
        token=r.json()["token"]
        return requests.get(API+path,headers={"Authorization":"Bearer "+token},timeout=3).json()
    except Exception as e:
        return {"error":str(e)}

def dashboard(request):
    return render(request,"dashboard.html",{"data":api_get("/api/dashboard")})

def customers(request):
    return render(request,"list.html",{"title":"Customers","items":api_get("/api/customers")})

def leads(request):
    return render(request,"list.html",{"title":"Leads","items":api_get("/api/leads")})

def opportunities(request):
    return render(request,"list.html",{"title":"Opportunities","items":api_get("/api/opportunities")})

def followups(request):
    return render(request,"list.html",{"title":"Follow-Ups","items":api_get("/api/followups")})
