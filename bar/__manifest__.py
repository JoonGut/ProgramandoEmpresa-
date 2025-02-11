# -*- coding: utf-8 -*-
{
    'name': "bar",

    'summary': "Gestion de pedidos de nuestro bar",

    'description': "App para la gestion de los pedidos de nuestros bar",

    'author': "Exides",
    'website': "https://www.exides.com",

    # Categories can be used to filter modules in modules listing
    # Check https://github.com/odoo/odoo/blob/15.0/odoo/addons/base/data/ir_module_category_data.xml
    # for the full list
    'category': 'Bar',
    'version': '0.1',

    # any module necessary for this one to work correctly
    'depends': ['base'],

    # always loaded
    'data': [
        'security/ir.model.access.csv',
        'views/views.xml',
        'views/templates.xml',
        'data/data.xml',
        'reports/repor_bar.xml',
    ],
    # only loaded in demonstration mode
    'demo': [
        'demo/demo.xml',
    ],
}

