--
-- Structure de la table ta_client_legrain
--

CREATE TABLE IF NOT EXISTS ta_client_legrain (
  id serial NOT NULL,
  code character varying(256) NOT NULL,
  nom character varying(256) NOT NULL,
  adresse1 character varying(256) DEFAULT NULL,
  adresse2 character varying(256) DEFAULT NULL,
  code_postal character varying(256) DEFAULT NULL,
  ville character varying(256) DEFAULT NULL,
  pays character varying(256) DEFAULT NULL,
  tva decimal(10,0) DEFAULT NULL,
  actif smallint DEFAULT NULL,
  date_creation timestamp with time zone DEFAULT NULL,
  date_dernier_engagement timestamp with time zone DEFAULT NULL,
  telephone1 character varying(256) DEFAULT NULL,
  telephone2 character varying(256) DEFAULT NULL,
  fax character varying(256) DEFAULT NULL,
  email character varying(256) DEFAULT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT 0,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_client_legrain
--

INSERT INTO ta_client_legrain (id, code, nom, adresse1, adresse2, code_postal, ville, pays, tva, actif, date_creation, date_dernier_engagement, telephone1, telephone2, fax, email, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 'LGR', 'Le Grain Informatique', '290 Avenue Charles de Gaulle', '', '82000', 'Montauban', 'France', 0, 1, '1970-01-01 00:00:00', '1970-01-01 00:00:00', '', '', '', '', NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table auth_url
--

CREATE TABLE IF NOT EXISTS ta_auth_url (
  id serial NOT NULL,
  id_role integer NOT NULL,
  url character varying(512) NOT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Contenu de la table auth_url
--

INSERT INTO ta_auth_url (id, id_role, url, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 2, '/b/', NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table auth_view
--

CREATE TABLE IF NOT EXISTS ta_auth_view (
  id serial NOT NULL,
  id_role integer NOT NULL,
  url character varying(512) NOT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL
);

-- --------------------------------------------------------

--
-- Structure de la table ta_groupe_entreprise
--

CREATE TABLE IF NOT EXISTS ta_groupe_entreprise (
  id serial NOT NULL,
  nom character varying(255) NOT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT 0,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_groupe_entreprise
--

INSERT INTO ta_groupe_entreprise (id, nom, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 'LGR_GRP', NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table ta_roles
--

CREATE TABLE IF NOT EXISTS ta_roles (
  id serial NOT NULL,
  role character varying(25) NOT NULL,
  description character varying(255) DEFAULT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_roles
--

INSERT INTO ta_roles (id, role, description, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 'admin', NULL, NULL, NULL, NULL, NULL, NULL, 0),
(2, 'utilisateur', NULL, NULL, NULL, NULL, NULL, NULL, 0),
(3, 'manager', '', NULL, NULL, NULL, NULL, NULL, 0),
(4, 'vendeur', '', NULL, NULL, NULL, NULL, NULL, 0),
(5, 'stockeur', '', NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table ta_entreprise_client
--

CREATE TABLE IF NOT EXISTS ta_entreprise_client (
  id serial NOT NULL,
  id_groupe_entreprise integer DEFAULT NULL,
  id_utilisateur integer DEFAULT NULL,
  id_client integer DEFAULT NULL,
  nom character varying(255) NOT NULL,
  adresse1 character varying(256) DEFAULT NULL,
  adresse2 character varying(256) DEFAULT NULL,
  code_postal character varying(256) DEFAULT NULL,
  ville character varying(256) DEFAULT NULL,
  departement character varying(256) DEFAULT NULL,
  region character varying(256) DEFAULT NULL,
  longitude character varying(256) DEFAULT NULL,
  latitude character varying(256) DEFAULT NULL,
  telephone character varying(256) DEFAULT NULL,
  fax character varying(256) DEFAULT NULL,
  email character varying(256) DEFAULT NULL,
  active smallint DEFAULT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_entreprise_client
--

INSERT INTO ta_entreprise_client (id, id_groupe_entreprise, id_utilisateur, id_client, nom, adresse1, adresse2, code_postal, ville, departement, region, longitude, latitude, telephone, fax, email, active, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 1, 1, 1, 'LGR', '290 Avenue Charles de Gaulle', '', '82000', 'Montauban', '82', NULL, '2.3522219', '48.856614', '05 63 30 31 44', '', 'legrainsas@gmail.com', 1, NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table ta_r_role_utilisateur
--

CREATE TABLE IF NOT EXISTS ta_r_role_utilisateur (
  id serial NOT NULL,
  id_utilisateur integer DEFAULT NULL,
  id_role integer NOT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_r_role_utilisateur
--

INSERT INTO ta_r_role_utilisateur (id, id_utilisateur, id_role, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 1, 1, NULL, NULL, NULL, NULL, NULL, 0),
(2, 2, 2, NULL, NULL, NULL, NULL, NULL, 0);

-- --------------------------------------------------------

--
-- Structure de la table ta_utilisateur
--

CREATE TABLE IF NOT EXISTS ta_utilisateur (
  id serial NOT NULL,
  username character varying(64) NOT NULL,
  passwd character varying(64) DEFAULT NULL,
  nom character varying(255) DEFAULT NULL,
  prenom character varying(255) DEFAULT NULL,
  email character varying(255) DEFAULT NULL,
  id_entreprise integer DEFAULT NULL,
  actif integer DEFAULT NULL,
  dernier_acces timestamp with time zone DEFAULT NULL,
  quand_cree timestamp with time zone DEFAULT NULL,
  quand_modif timestamp with time zone DEFAULT NULL,
  qui_cree integer DEFAULT NULL,
  qui_modif integer DEFAULT NULL,
  ip_acces character varying(512) DEFAULT NULL,
  version_obj integer DEFAULT NULL,
  PRIMARY KEY (id)
);

--
-- Contenu de la table ta_utilisateur
--

INSERT INTO ta_utilisateur (id, username, passwd, nom, prenom, email, id_entreprise, actif, dernier_acces, quand_cree, quand_modif, qui_cree, qui_modif, ip_acces, version_obj) VALUES
(1, 'nicolas', 'VLICnfhhmeKqsofW4g8gD18GuPR1U6PBjf/1BgoeYW4=', 'Mura', 'Nicolas', NULL, 1, NULL,'2014-12-03 14:57:04', NULL, '2014-12-03 14:57:04', 1, 1, '127.0.0.1', 0),
(2, 'legrain', 'CPb1ngVAlEYiyUF2zQ0SnyKLNRvBQWnBB1MLc+FcC70=', 'legrain', NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0);

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table ta_entreprise_client
--
ALTER TABLE ta_entreprise_client
  ADD CONSTRAINT ta_entreprise_client_ibfk_1 FOREIGN KEY (id_groupe_entreprise) REFERENCES ta_groupe_entreprise (id),
  ADD CONSTRAINT ta_entreprise_client_ibfk_2 FOREIGN KEY (id_client) REFERENCES ta_client_legrain (id);

--
-- Contraintes pour la table ta_r_role_utilisateur
--
ALTER TABLE ta_r_role_utilisateur
  ADD CONSTRAINT ta_r_role_utilisateur_ibfk_1 FOREIGN KEY (id_utilisateur) REFERENCES ta_utilisateur (id),
  ADD CONSTRAINT ta_r_role_utilisateur_ibfk_2 FOREIGN KEY (id_role) REFERENCES ta_roles (id);

--
-- Contraintes pour la table ta_utilisateur
--
ALTER TABLE ta_utilisateur
  ADD CONSTRAINT ta_utilisateur_ibfk_5 FOREIGN KEY (id_entreprise) REFERENCES ta_entreprise_client (id);
