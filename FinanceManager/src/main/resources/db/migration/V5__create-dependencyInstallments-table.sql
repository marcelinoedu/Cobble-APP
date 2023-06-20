CREATE TABLE dependency_installments(
  installment_id INT PRIMARY KEY AUTO_INCREMENT,
  amount NUMERIC(10,2) NOT NULL,
  deadline DATE NOT NULL,
  dependency_id INT NOT NULL,
  paid BOOLEAN DEFAULT FALSE,
  paid_at TIMESTAMP NULL,
  FOREIGN KEY (dependency_id) REFERENCES dependencies (id)
);