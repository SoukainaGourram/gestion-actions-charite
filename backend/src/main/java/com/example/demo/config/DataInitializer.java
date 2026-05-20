package com.example.demo.config;

import com.example.demo.entities.Action;
import com.example.demo.entities.Donation;
import com.example.demo.entities.Organisation;
import com.example.demo.entities.User;
import com.example.demo.repos.ActionRepository;
import com.example.demo.repos.DonationRepository;
import com.example.demo.repos.OrganisationRepository;
import com.example.demo.repos.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final OrganisationRepository orgRepo;
    private final ActionRepository actionRepo;
    private final DonationRepository donationRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo,
                           OrganisationRepository orgRepo,
                           ActionRepository actionRepo,
                           DonationRepository donationRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.orgRepo = orgRepo;
        this.actionRepo = actionRepo;
        this.donationRepo = donationRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() > 0) {
            System.out.println("La base de données contient déjà des données. Seeding annulé.");
            return;
        }

        System.out.println("Démarrage du Seeding des données HopeConnect...");

        // 1. ORGANISATIONS
        Organisation orgNour = new Organisation();
        orgNour.setName("Association Nour pour l'Éducation");
        orgNour.setAddress("Angle Rue Jbel Toubkal et Rue Ghandi, Casablanca");
        orgNour.setFiscalNumber("IF-98765432-N");
        orgNour.setContact("+212 522-457812");
        orgNour.setLogoUrl("https://images.unsplash.com/photo-1427504494785-3a9ca7044f45?w=200");
        orgNour.setDescription("Une organisation dédiée au soutien scolaire des orphelins et à l'accès équitable à l'éducation dans les zones rurales du Maroc.");
        orgNour.setApproved(true);
        orgNour = orgRepo.save(orgNour);

        Organisation orgMedical = new Organisation();
        orgMedical.setName("Fondation Solidarité Médicale");
        orgMedical.setAddress("14 Boulevard Zerktouni, Marrakech");
        orgMedical.setFiscalNumber("IF-12345678-M");
        orgMedical.setContact("+212 524-336699");
        orgMedical.setLogoUrl("https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=200");
        orgMedical.setDescription("Fournit des soins médicaux gratuits, organise des caravanes chirurgicales et soutient les hôpitaux ruraux à travers le royaume.");
        orgMedical.setApproved(true);
        orgMedical = orgRepo.save(orgMedical);

        Organisation orgVert = new Organisation();
        orgVert.setName("Maroc Vert & Propre");
        orgVert.setAddress("Avenue Mohammed V, Rabat");
        orgVert.setFiscalNumber("IF-45678912-V");
        orgVert.setContact("+212 537-889900");
        orgVert.setLogoUrl("https://images.unsplash.com/photo-1466692476868-aef1dfb1e735?w=200");
        orgVert.setDescription("Œuvre pour le reboisement des forêts de l'Atlas, le nettoyage des plages marocaines et l'installation de puits alimentés par l'énergie solaire.");
        orgVert.setApproved(true);
        orgVert = orgRepo.save(orgVert);

        // 2. ACTIONS
        Action actionOrphelins = new Action();
        actionOrphelins.setTitle("Soutien Scolaire et Cartables pour 200 Orphelins");
        actionOrphelins.setDescription("Cette action vise à fournir des cartables complets avec fournitures scolaires, manuels, tablettes éducatives et un programme d'accompagnement scolaire annuel pour 200 orphelins dans la région de Tahanaout.");
        actionOrphelins.setLocation("Tahanaout, Région d'Al Haouz");
        actionOrphelins.setGoalAmount(50000.0);
        actionOrphelins.setCategory("education");
        actionOrphelins.setStartDate(LocalDate.now().minusDays(20));
        actionOrphelins.setEndDate(LocalDate.now().plusMonths(4));
        actionOrphelins.setImageUrl("https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=600");
        actionOrphelins.setStatus("ACTIVE");
        actionOrphelins.setOrganisation(orgNour);
        actionOrphelins = actionRepo.save(actionOrphelins);

        Action actionCaravane = new Action();
        actionCaravane.setTitle("Caravane Chirurgicale Ophtalmologique dans l'Atlas");
        actionCaravane.setDescription("Organisation d'une caravane médicale et chirurgicale pour traiter la cataracte et distribuer des lunettes correctrices pour plus de 500 personnes âgées dans les villages isolés du Haut Atlas.");
        actionCaravane.setLocation("Imilchil, Province de Midelt");
        actionCaravane.setGoalAmount(120000.0);
        actionCaravane.setCategory("sante");
        actionCaravane.setStartDate(LocalDate.now().minusDays(10));
        actionCaravane.setEndDate(LocalDate.now().plusMonths(2));
        actionCaravane.setImageUrl("https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=600");
        actionCaravane.setStatus("ACTIVE");
        actionCaravane.setOrganisation(orgMedical);
        actionCaravane = actionRepo.save(actionCaravane);

        Action actionPuits = new Action();
        actionPuits.setTitle("Forage de 3 Puits Solaires dans le Sud");
        actionPuits.setDescription("Construction de trois puits d'eau potable profonds équipés de pompes solaires et de fontaines de distribution pour désenclaver les familles et lutter contre la sécheresse dans trois douars de la province de Tata.");
        actionPuits.setLocation("Province de Tata");
        actionPuits.setGoalAmount(150000.0);
        actionPuits.setCategory("environnement");
        actionPuits.setStartDate(LocalDate.now().minusDays(35));
        actionPuits.setEndDate(LocalDate.now().plusMonths(3));
        actionPuits.setImageUrl("https://images.unsplash.com/photo-1465146344425-f00d5f5c8f07?w=600");
        actionPuits.setStatus("ACTIVE");
        actionPuits.setOrganisation(orgVert);
        actionPuits = actionRepo.save(actionPuits);

        Action actionPaniers = new Action();
        actionPaniers.setTitle("Paniers Alimentaires de Solidarité");
        actionPaniers.setDescription("Distribution de paniers de nourriture complets (farine, huile, thé, sucre, dattes, légumineuses) pour soutenir 500 familles nécessiteuses et veuves dans les zones défavorisées de Casablanca.");
        actionPaniers.setLocation("Casablanca (Sidi Moumen)");
        actionPaniers.setGoalAmount(75000.0);
        actionPaniers.setCategory("alimentaire");
        actionPaniers.setStartDate(LocalDate.now().minusDays(5));
        actionPaniers.setEndDate(LocalDate.now().plusMonths(1));
        actionPaniers.setImageUrl("https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?w=600");
        actionPaniers.setStatus("ACTIVE");
        actionPaniers.setOrganisation(orgNour);
        actionPaniers = actionRepo.save(actionPaniers);

        // 3. UTILISATEURS
        User superAdmin = new User();
        superAdmin.setName("Super Admin HopeConnect");
        superAdmin.setEmail("admin@hopeconnect.ma");
        superAdmin.setPassword(passwordEncoder.encode("admin123"));
        superAdmin.setRole("SUPER_ADMIN");
        superAdmin.setPhone("+212 600-000001");
        superAdmin = userRepo.save(superAdmin);

        User orgAdmin = new User();
        orgAdmin.setName("Responsable Association Nour");
        orgAdmin.setEmail("nour.admin@hopeconnect.ma");
        orgAdmin.setPassword(passwordEncoder.encode("nour123"));
        orgAdmin.setRole("ORG_ADMIN");
        orgAdmin.setPhone("+212 600-000002");
        orgAdmin.setOrganisation(orgNour);
        orgAdmin = userRepo.save(orgAdmin);

        User userYassine = new User();
        userYassine.setName("Yassine Bennani");
        userYassine.setEmail("yassine.bennani@gmail.com");
        userYassine.setPassword(passwordEncoder.encode("password"));
        userYassine.setRole("USER");
        userYassine.setPhone("+212 661-457812");
        userYassine = userRepo.save(userYassine);

        User userFatima = new User();
        userFatima.setName("Fatima-Zahra El Alami");
        userFatima.setEmail("fatima.zahra@gmail.com");
        userFatima.setPassword(passwordEncoder.encode("password"));
        userFatima.setRole("USER");
        userFatima.setPhone("+212 662-889977");
        userFatima = userRepo.save(userFatima);

        User userSamir = new User();
        userSamir.setName("Samir Idrissi");
        userSamir.setEmail("samir.idrissi@gmail.com");
        userSamir.setPassword(passwordEncoder.encode("password"));
        userSamir.setRole("USER");
        userSamir.setPhone("+212 663-112233");
        userSamir = userRepo.save(userSamir);

        User userAmal = new User();
        userAmal.setName("Amal Naciri");
        userAmal.setEmail("amal.naciri@gmail.com");
        userAmal.setPassword(passwordEncoder.encode("password"));
        userAmal.setRole("USER");
        userAmal.setPhone("+212 664-556677");
        userAmal = userRepo.save(userAmal);

        // 4. DONS RÉALISTES
        // Don 1 : Yassine sur Forage de 3 Puits Solaires
        Donation don1 = new Donation();
        don1.setAmount(1500.0);
        don1.setPaymentMethod("STRIPE");
        don1.setStatus("COMPLETED");
        don1.setUser(userYassine);
        don1.setAction(actionPuits);
        don1.setDonationDate(LocalDateTime.now().minusDays(15));
        donationRepo.save(don1);

        // Don 2 : Fatima-Zahra sur Soutien Scolaire
        Donation don2 = new Donation();
        don2.setAmount(500.0);
        don2.setPaymentMethod("STRIPE");
        don2.setStatus("COMPLETED");
        don2.setUser(userFatima);
        don2.setAction(actionOrphelins);
        don2.setDonationDate(LocalDateTime.now().minusDays(12));
        donationRepo.save(don2);

        // Don 3 : Samir sur Caravane Ophtalmologique
        Donation don3 = new Donation();
        don3.setAmount(5000.0);
        don3.setPaymentMethod("STRIPE");
        don3.setStatus("COMPLETED");
        don3.setUser(userSamir);
        don3.setAction(actionCaravane);
        don3.setDonationDate(LocalDateTime.now().minusDays(8));
        donationRepo.save(don3);

        // Don 4 : Amal sur Paniers Alimentaires
        Donation don4 = new Donation();
        don4.setAmount(1200.0);
        don4.setPaymentMethod("STRIPE");
        don4.setStatus("COMPLETED");
        don4.setUser(userAmal);
        don4.setAction(actionPaniers);
        don4.setDonationDate(LocalDateTime.now().minusDays(4));
        donationRepo.save(don4);

        // Don 5 : Yassine sur Caravane Ophtalmologique
        Donation don5 = new Donation();
        don5.setAmount(3000.0);
        don5.setPaymentMethod("STRIPE");
        don5.setStatus("COMPLETED");
        don5.setUser(userYassine);
        don5.setAction(actionCaravane);
        don5.setDonationDate(LocalDateTime.now().minusDays(2));
        donationRepo.save(don5);

        // Don 6 : Fatima-Zahra sur Forage de 3 Puits Solaires
        Donation don6 = new Donation();
        don6.setAmount(2500.0);
        don6.setPaymentMethod("STRIPE");
        don6.setStatus("COMPLETED");
        don6.setUser(userFatima);
        don6.setAction(actionPuits);
        don6.setDonationDate(LocalDateTime.now().minusDays(1));
        donationRepo.save(don6);

        // Don 7 : Samir sur Soutien Scolaire
        Donation don7 = new Donation();
        don7.setAmount(800.0);
        don7.setPaymentMethod("STRIPE");
        don7.setStatus("COMPLETED");
        don7.setUser(userSamir);
        don7.setAction(actionOrphelins);
        don7.setDonationDate(LocalDateTime.now().minusHours(4));
        donationRepo.save(don7);

        // 5. METTRE À JOUR LES MONTANTS DES ACTIONS
        updateActionTotalAmount(actionOrphelins);
        updateActionTotalAmount(actionCaravane);
        updateActionTotalAmount(actionPuits);
        updateActionTotalAmount(actionPaniers);

        System.out.println("Seeding des données terminé avec succès !");
    }

    private void updateActionTotalAmount(Action action) {
        Double total = donationRepo.findByActionIdAndStatus(action.getId(), "COMPLETED")
                .stream()
                .mapToDouble(Donation::getAmount)
                .sum();
        action.setCurrentAmount(total);
        actionRepo.save(action);
    }
}
