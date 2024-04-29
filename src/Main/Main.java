package Main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        String msg = "<html><div style='text-align: justify; width: 300px;'>" +
                "Bienvenue dans le jeu : \"The shadow in search of light\"<br/>" +
                "Plongez-vous dans un conte ensorcelant où vous endosserez le rôle d'une ombre, autrefois un fier homme, jusqu'à ce qu'un funeste jour une sorcière maléfique, avide de jeux cruels, vous lance un sortilège qui vous métamorphose en une ombre vivante, arrachant votre forme humaine. Mais ne soyez point accablé par le désespoir, car une fée, spectatrice de cette tragédie, émerge du ciel.<br/><br/>" +
                "Alors que la sorcière se téléporte dans les ténèbres, la fée se manifeste et vous dévoile qu'un espoir subsiste : le fruit de l'Arbre de la Pleine Lune détient le pouvoir de briser ce maléfice. Cet arbre enchanté offre un seul fruit à chaque pleine lune et prospère uniquement au cœur du Labyrinthe Lumineux, une contrée mystique où la magie danse à chaque détour.<br/><br/>" +
                "Guidé par la lumière féerique de la fée, vous vous engagez dans ce dédale enchanté.<br/>Votre quête : trouver l'Arbre de la Pleine Lune et récolter son précieux fruit salvateur qui vous rendra votre apparence humaine, libérant ainsi votre être de l'ombre qui le retient prisonnier.<br/><br/>" +
                "Contrôlez le personnage avec les touches directionnelles.<br/><br/>" +
                "Ce programme ainsi que les sprites ont été réalisés par GUNDUZ Maxime. Vous pouvez trouver ces travaux sur GitHub :<br/> <a href='https://github.com/MaxiyaG'>https://github.com/MaxiyaG</a>.<br/>Merci de le citer pour l’utilisation des sprites.<br/><br/>" +
                "Voulez-vous commencer le jeu?</div></html>";


        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int choice = JOptionPane.showConfirmDialog(window, msg, "Êtes-vous prêt ?", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            window.setResizable(false);
            window.setTitle("The shadow in search of light");

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);
            window.pack();

            window.setLocationRelativeTo(null);

            window.setVisible(true);
            gamePanel.startGameThread();
        } else {
            System.exit(0);
        }
    }
}
