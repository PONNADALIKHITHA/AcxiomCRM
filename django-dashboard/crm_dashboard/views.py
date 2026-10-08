import requests
from django.shortcuts import render
from django.http import HttpResponse

API = "http://localhost:8080"

def normalize_record(item):
    if not isinstance(item, dict):
        return item

    name = item.get("name") or item.get("followUpType") or item.get("stage") or "-"
    email_or_date = item.get("email") or item.get("followUpDate") or item.get("expectedCloseDate") or "-"
    status_or_stage = item.get("status") or item.get("stage") or "-"
    detail = (
        item.get("companyName")
        or item.get("remarks")
        or item.get("amount")
        or item.get("expectedValue")
        or item.get("assignedTo")
        or "-"
    )

    normalized = dict(item)
    normalized["display_name"] = name
    normalized["display_email_or_date"] = email_or_date
    normalized["display_status"] = status_or_stage
    normalized["display_detail"] = detail
    return normalized


def api_get(path):
    try:
        r=requests.post(API+"/api/auth/login",json={"username":"admin","password":"Admin@123"},timeout=3)
        token=r.json()["token"]
        payload = requests.get(API+path,headers={"Authorization":"Bearer "+token},timeout=3).json()
        if isinstance(payload, dict) and "error" not in payload and isinstance(payload.get("value"), list):
            payload["value"] = [normalize_record(x) for x in payload["value"]]
        elif isinstance(payload, list):
            payload = [normalize_record(x) for x in payload]
        return payload
    except Exception as e:
        return {"error":str(e)}

def extract_items(payload):
    if isinstance(payload, list):
        return payload
    if isinstance(payload, dict):
        if isinstance(payload.get("value"), list):
            return payload["value"]
        if isinstance(payload.get("items"), list):
            return payload["items"]
        return []
    return []


def dashboard(request):
    return render(request,"dashboard.html",{"data":api_get("/api/dashboard")})

def customers(request):
    return render(request,"list.html",{"title":"Customers","items":extract_items(api_get("/api/customers"))})

def leads(request):
    return render(request,"list.html",{"title":"Leads","items":extract_items(api_get("/api/leads"))})

def opportunities(request):
    return render(request,"list.html",{"title":"Opportunities","items":extract_items(api_get("/api/opportunities"))})

def followups(request):
    return render(request,"list.html",{"title":"Follow-Ups","items":extract_items(api_get("/api/followups"))})
