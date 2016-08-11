import java.awt.Container; 
import java.awt.Font; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
import java.io.IOException; 
  

import javax.swing.ImageIcon; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
import javax.swing.JLabel;
import javax.swing.JOptionPane; 
  
public class GUI extends JFrame implements ActionListener{ 
    JButton trussButton, dynamicButton, electricButton, fourButtonI, fourButtonII; 
    JFrame runningScreen;
    JLabel msg;
      
    public GUI(){ 
        super("Select your problem"); 
          
        this.setSize(300, 520); 
        Container c = this.getContentPane(); 
        c.setLayout(null); 
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); 
          
        trussButton = new JButton(new ImageIcon("Images/TRUSS.png")); 
        trussButton.setFont(new Font("Arial", Font.PLAIN, 35)); 
        trussButton.setBounds(0,0,300,100); 
        trussButton.addActionListener(this); 
        c.add(trussButton); 
          
        dynamicButton = new JButton(new ImageIcon("Images/MSD.png")); 
        dynamicButton.setFont(new Font("Arial", Font.PLAIN, 35)); 
        dynamicButton.setBounds(0,100,300,100); 
        dynamicButton.addActionListener(this); 
        c.add(dynamicButton); 
          
        electricButton = new JButton(new ImageIcon("Images/Electric.png")); 
        electricButton.setFont(new Font("Arial", Font.PLAIN, 35)); 
        electricButton.setBounds(0,200,300,100); 
        electricButton.addActionListener(this); 
        c.add(electricButton); 
          
        fourButtonI = new JButton(new ImageIcon("Images/4.1.png")); 
        fourButtonI.setFont(new Font("Arial", Font.PLAIN, 35)); 
        fourButtonI.setBounds(0,300,300,100); 
        fourButtonI.addActionListener(this); 
        c.add(fourButtonI); 
  
        fourButtonII = new JButton(new ImageIcon("Images/4.2.png")); 
        fourButtonII.setFont(new Font("Arial", Font.PLAIN, 35)); 
        fourButtonII.setBounds(0,400,300,100); 
        fourButtonII.addActionListener(this); 
        c.add(fourButtonII); 
        this.setVisible(true); 
        

    	runningScreen=new JFrame("Running");
    	runningScreen.setSize(500, 200);
    	JLabel msg = new JLabel(" Solver is running. Please wait.");
    	msg.setFont(new Font("Arial", Font.PLAIN, 35)); 
    	runningScreen.getContentPane().add(msg);
    } 
    
    public void running(){
    	runningScreen.setVisible(true);
    }
    public void done(){
    	runningScreen.setVisible(false);
    	JOptionPane.showMessageDialog(null, "Solver has finished executing.");
    }
  
    public void actionPerformed(ActionEvent e) { 
    	//done();
        String fileName=JOptionPane.showInputDialog("Please input the problem data filename (ex trussInput.csv)"); 
        if(fileName!=null){ 
            if(e.getSource()==trussButton){ 
                try { 
                    PhysicalProblem p = new PhysicalProblem(fileName, true); 
                    running();
                    Solver.TrussStress(p.getElements(), p.getNodes());
                    done();
                }catch (BadInputException e1) { 
                    JOptionPane.showMessageDialog(null, "error in input"); 
                    e1.printStackTrace(); 
                }  catch (IOException e1) { 
                    JOptionPane.showMessageDialog(null, "Error reading file"); 
                    e1.printStackTrace(); 
                } 
                  
            } 
            else if(e.getSource()==dynamicButton){ 
                try { 
                    PhysicalProblem p = new PhysicalProblem(fileName, false); 
                    running();
                    Solver.solveMassSpringDamper(p.getNodes(), p.getElements(), p.getTime1(), p.getTime2(), p.getTimeStep(), Solver.Q3);     
                    done();
                }catch (BadInputException e1) { 
                    JOptionPane.showMessageDialog(null, "error in input"); 
                    e1.printStackTrace(); 
                }  catch (IOException e1) { 
                    JOptionPane.showMessageDialog(null, "Error reading file"); 
                    e1.printStackTrace(); 
                } 
            } 
            else if(e.getSource()==electricButton){ 
                try {
					ElectricProblem p = new ElectricProblem(fileName);
					running();
					Solver.solveElectrical(p.getElements());
					done();
                }catch (BadInputException e1) { 
                    JOptionPane.showMessageDialog(null, "error in input"); 
                    e1.printStackTrace(); 
                }  catch (IOException e1) { 
                    JOptionPane.showMessageDialog(null, "Error reading file"); 
                    e1.printStackTrace(); 
                } 
                
            } 
            else if(e.getSource()==fourButtonI){ 
                try { 
                    PhysicalProblem p = new PhysicalProblem(fileName, false); 
                    running();
                    Solver.solveMassSpringDamper(p.getNodes(), p.getElements(), p.getTime1(), p.getTime2(), 0.000025, Solver.Q4I);
                    done();
                }catch (BadInputException e1) { 
                    JOptionPane.showMessageDialog(null, "error in input"); 
                    e1.printStackTrace(); 
                }  catch (IOException e1) { 
                    JOptionPane.showMessageDialog(null, "Error reading file"); 
                    e1.printStackTrace(); 
                } 
            }    
            else if(e.getSource()==fourButtonII){ 
                try { 
                    PhysicalProblem p = new PhysicalProblem(fileName, false); 
                    running();
                    Solver.solveMassSpringDamper(p.getNodes(), p.getElements(), p.getTime1(), p.getTime2(), p.getTimeStep(), Solver.Q4II);  
                    done();
                }catch (BadInputException e1) { 
                    JOptionPane.showMessageDialog(null, "error in input"); 
                    e1.printStackTrace(); 
                }  catch (IOException e1) { 
                    JOptionPane.showMessageDialog(null, "Error reading file"); 
                    e1.printStackTrace(); 
                } 
            } 
        } 
    } 
}