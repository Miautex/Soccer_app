-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 20. Mrz 2017 um 15:40
-- Server Version: 5.5.54
-- PHP-Version: 5.5.23-1+deb.sury.org~precise+2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `4BHIFS_2017_SOCCER_G2`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `games`
--

CREATE TABLE IF NOT EXISTS `games` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `scoreTeamA` int(10) unsigned NOT NULL,
  `scoreTeamB` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Daten für Tabelle `games`
--

INSERT INTO `games` (`id`, `date`, `scoreTeamA`, `scoreTeamB`) VALUES
(1, '2017-03-16', 5, 1),
(2, '2017-03-15', 2, 2),
(3, '2017-03-14', 0, 0),
(4, '2017-03-12', 1, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `participation`
--

CREATE TABLE IF NOT EXISTS `participation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idGame` int(11) NOT NULL,
  `idPlayer` int(11) NOT NULL,
  `goalsGot` int(11) NOT NULL,
  `goalsShotDefault` int(11) NOT NULL,
  `goalsShotHead` int(11) NOT NULL,
  `goalsShotHeadSnow` int(11) NOT NULL,
  `goalsShotPenalty` int(11) NOT NULL,
  `nutmeg` int(11) NOT NULL,
  `team` enum('A','B','UNASSIGNED') NOT NULL DEFAULT 'UNASSIGNED',
  PRIMARY KEY (`id`),
  KEY `idGame` (`idGame`),
  KEY `idPlayer` (`idPlayer`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Daten für Tabelle `participation`
--

INSERT INTO `participation` (`id`, `idGame`, `idPlayer`, `goalsGot`, `goalsShotDefault`, `goalsShotHead`, `goalsShotHeadSnow`, `goalsShotPenalty`, `nutmeg`, `team`) VALUES
(3, 1, 1, 0, 0, 0, 0, 0, 0, 'UNASSIGNED');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `players`
--

CREATE TABLE IF NOT EXISTS `players` (
  `ID` int(10) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `ISADMIN` tinyint(1) NOT NULL DEFAULT '0',
  `PASSWORD` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Daten für Tabelle `players`
--

INSERT INTO `players` (`ID`, `USERNAME`, `NAME`, `ISADMIN`, `PASSWORD`) VALUES
(1, 'martin', 'Martin', 0, 'a6444a246547facadfbc56ed7a940b7f'),
(2, 'elias', 'Elias', 0, 'b0c5fe61c99681f127d708165717adcf'),
(3, 'marco', 'Marco', 1, 'ef51e2c67b33239dd3c01077cb70008d'),
(4, 'raphael', 'Raphael', 1, 'e01c91e255651ddcbaa452da17dfb87c'),
(5, 'pascal', 'Pascal', 0, 'effd2a3792d289e2dae4490d8b71c1c7'),
(6, 'jakob', 'Jakob', 0, '46b92d9fc6d4824efdac9c329feaf7da'),
(7, 'stefan', 'Stefan', 0, 'f242543cbbdaeb0b618011bd03701d10'),
(8, 'lukas', 'Lukas', 0, 'd3e293c8c3ec9410aa418ce6cecceb04');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `positions`
--

CREATE TABLE IF NOT EXISTS `positions` (
  `id_player` int(11) NOT NULL,
  `position` enum('GOAL','MIDFIELD','DEFENSE','ATTACK') NOT NULL,
  PRIMARY KEY (`id_player`,`position`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `positions`
--

INSERT INTO `positions` (`id_player`, `position`) VALUES
(1, 'GOAL'),
(2, 'MIDFIELD'),
(3, 'DEFENSE'),
(4, 'ATTACK'),
(5, 'GOAL'),
(6, 'MIDFIELD'),
(7, 'MIDFIELD'),
(7, 'DEFENSE'),
(8, 'DEFENSE'),
(8, 'ATTACK');

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `participation`
--
ALTER TABLE `participation`
  ADD CONSTRAINT `participation_ibfk_1` FOREIGN KEY (`idGame`) REFERENCES `games` (`id`),
  ADD CONSTRAINT `participation_ibfk_2` FOREIGN KEY (`idPlayer`) REFERENCES `players` (`ID`);

--
-- Constraints der Tabelle `positions`
--
ALTER TABLE `positions`
  ADD CONSTRAINT `positions_ibfk_1` FOREIGN KEY (`id_player`) REFERENCES `players` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
