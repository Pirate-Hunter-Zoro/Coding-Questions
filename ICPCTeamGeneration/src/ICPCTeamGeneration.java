import java.io.*;
import java.util.*;

/**
 * Vi is a coach for her university’s ICPC organization and is working on
 * creating teams for their upcoming regional contest. They recently competed in
 * the North America Qualifier and Vi is using the results as well as each
 * person’s preferences to create as many teams of three as possible to send to
 * regionals.
 * 
 * More specifically, people from Vi’s university competed in the North America
 * Qualifier (NAQ), and each person got a unique rank from to . The person at
 * rank has two parameters, and , where , indicating that their two teammates
 * must have a rank between and , inclusive. Teams must have exactly three
 * people.
 * 
 * Due to the collaborative environment, Vi notes that for every pair of
 * individuals at ranks and , if , then and .
 * 
 * Compute the maximum number of teams that Vi can send to regionals
 * 
 * Input
 * 
 * The first line of input contains a single integer (), which is the number of
 * competitors in the local contest.
 * 
 * Each of the next lines contains two integers and (), where is the
 * competitor’s rank. These are the limits of the ranks of the competitors that
 * can be teamed with this competitor. The competitors are given in rank order,
 * from to . If , then and .
 * 
 * Output
 * 
 * Output a single integer, which is the maximum number of teams Vi can send to
 * the regional contest.
 * 
 * Link:
 * https://open.kattis.com/problems/icpcteamgeneration
 */
public class ICPCTeamGeneration {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
    }
}
