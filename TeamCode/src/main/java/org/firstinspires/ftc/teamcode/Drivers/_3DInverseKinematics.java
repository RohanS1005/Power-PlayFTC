package org.firstinspires.ftc.teamcode.Drivers;

public class _3DInverseKinematics {
    double x;
    double y;
    double z;
    double a;
    double b;
    double c;
    double p;
    double q;

    public _3DInverseKinematics(double x,double y,double z, double a, double b, double c){
        this.x=x;
        this.y=y;
        this.z=z;
        this.a=a;
        this.b=b;
        this.c=c;
    }
    /*
    (𝑞2,𝑞3)∈2𝑅𝐼𝐾(𝐿2,𝐿3,(√𝑥^2+𝑦^2,−𝑧𝐷+𝐿1))
    (𝑞′2,𝑞′3)∈2𝑅𝐼𝐾(𝐿2,𝐿3,(−√𝑥^2+𝑦^2,−𝑧𝐷+𝐿1))
    𝑐2={‖𝐱𝐷‖^2−𝐿(1)^2−𝐿(2)^2}/2𝐿(1_𝐿(2)
    k - axis with length of arm in xy axis as its coord
     */
    public double[][] getAng(){

        System.out.println("Entered getAng");
        double[][] ang = new double[2][3];
        ang[0][0] = get_aAngle();

        if (Math.abs(get_2dIK())>1){
            System.out.println("Entered first if");
            ang[0][0]=0; //finalize what to put at null configuration
            return ang;
        }

        else if(get_2dIK()==1){
            System.out.println("Entered second if");
            ang[0][1]= Math.atan((z-a)/(Math.sqrt((x*x)+(y*y))));
            ang[0][2]=0;
        }

        else if(get_2dIK()==-1 && Math.sqrt((x*x)+(y*y)+(z*z)-(2*a*z)+(a*a))!=0){
            System.out.println("Entered third if");
            ang[0][1]= Math.atan((Math.sqrt((x*x)+(y*y)))/(z-a));
            ang[0][2]=Math.PI;
        }

        else{
            System.out.println("Entered fourth if");
            ang[0][1]= positiveLastCase();
            ang[0][2] = Math.acos(get_2dIK());
            ang[1][0]=get_aAngle();
            ang[1][1] = negativeLastCase();
            ang[1][2] = -Math.acos(get_2dIK());
        }

        return ang;
    }

    public double positiveLastCase(){
        double posQ2 = Math.acos(get_2dIK());
        double posP2 = Math.atan2((z-a),(Math.sqrt((x*x)+(y*y)))) - Math.atan2(c*Math.sin(posQ2),b+c*Math.cos(posQ2));
        return posP2;
    }

    public double negativeLastCase(){
        double negQ2 = -Math.acos(get_2dIK());
        double negP2 = Math.atan2((z-a),(Math.sqrt((x*x)+(y*y)))) - Math.atan2(c*Math.sin(negQ2),b+c*Math.cos(negQ2));
        return negP2;
    }

    public double get_aAngle(){
        double aAngle = 0;
        aAngle = Math.atan2(y,x)-(Math.PI/2);
        System.out.println("get_aAngle: y: " + y + " x: " + x);
        return aAngle;
    }

    //(√𝑥^2+𝑦^2)^2 + (-z+a)^2
    public double get_2dIK(){
        p = ((x*x)+(y*y)+(z*z)-(2*a*z)+(a*a) - (b*b) - (c*c))/(2*c*b);
        return p;
    }
}
