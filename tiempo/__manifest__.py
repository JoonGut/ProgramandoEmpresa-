# -*- coding: utf-8 -*-
{
    'name': "Tiempo",

    'summary': "Modulo para la gestión de la prevision del tiempo",

    'description': "Modulo para la gestión de la prevision del tiempo",

    'author': "Exides",
    'website': "https://www.exides.com",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/15.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Meteorologia',
    'version': '0.1',

    # any module necessary for this one to work correctly
    'depends': ['base'],

    # always loaded
    'data': [
        'security/ir.model.access.csv',
        'views/views.xml',
        'views/templates.xml',
        'reports/report_previsiones.xml',
        'data/data.xml',
    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/demo.xml',
    ],
}

