@echo off
cd django-dashboard
if not exist venv python -m venv venv
call venv\Scripts\activate
pip install -r requirements.txt
python manage.py migrate
python manage.py runserver 8000
