-- Crear tabla de productos
CREATE TABLE IF NOT EXISTS products (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL NOT NULL,
    stock INTEGER NOT NULL,
    supplier_name TEXT
);

-- Crear tabla de proveedores
CREATE TABLE IF NOT EXISTS suppliers (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    contact_person TEXT,
    phone TEXT,
    email TEXT,
    address TEXT
);

-- Crear tabla de facturas
CREATE TABLE IF NOT EXISTS invoices (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT NOT NULL,
    customer_name TEXT,
    total REAL NOT NULL,
    status TEXT NOT NULL
);

-- Crear tabla de detalles de factura
CREATE TABLE IF NOT EXISTS invoice_details (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    invoice_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Crear tabla de cuentas por pagar
CREATE TABLE IF NOT EXISTS accounts (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    details TEXT NOT NULL,
    amount REAL NOT NULL,
    due_date TEXT,
    status TEXT NOT NULL
);

-- Insertar algunos productos de muestra
INSERT INTO products (name, description, price, stock, supplier_name) VALUES
('Papel A4', 'Resma de papel A4 de 500 hojas', 5.99, 100, 'Papelería Central'),
('Tóner Negro', 'Tóner para impresora HP LaserJet', 45.50, 20, 'Suministros Globales'),
('Tóner Color', 'Tóner a color para impresora HP Color LaserJet', 65.75, 15, 'Suministros Globales'),
('Carpeta archivadora', 'Carpeta de anillas tamaño A4', 3.25, 50, 'Papelería Central'),
('Bolígrafos', 'Paquete de 10 bolígrafos azules', 4.99, 30, 'Importadora del Este');

-- Insertar algunos proveedores de muestra
INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES
('Papelería Central', 'Juan Pérez', '555-1234', 'jperez@papeleriacentral.com', 'Calle Principal 123'),
('Suministros Globales', 'María Gómez', '555-5678', 'mgomez@suministrosglobales.com', 'Avenida Central 456'),
('Importadora del Este', 'Carlos Rodríguez', '555-9012', 'crodriguez@importadoraeste.com', 'Calle Secundaria 789'); 