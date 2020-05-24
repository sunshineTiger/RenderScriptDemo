#pragma version(1)
#pragma rs java_package_name(render.test)
#pragma rs_fp_relaxed
#include "rs_debug.rsh"
rs_allocation gIntFrame;
float radians_rot;
 float transwidht;
 float transheight;
static rs_matrix4x4 matrixa;
static rs_matrix4x4 matrixb;
static rs_matrix4x4 matrixc;
static rs_matrix4x4 matrixe;
//1520  855
void init(){

}

void initMatrixb(){
        rsMatrixLoadRotate (&matrixa, radians_rot, 0, 0, 1);//正：顺时针  负：逆时针
        rsMatrixLoadTranslate(&matrixb,-(transwidht-1)/2,-(transheight-1)/2,0 );
        rsMatrixLoadTranslate(&matrixc,(transheight-1)/2,(transwidht-1)/2,0 );
        rsDebug("---------------init0------->>:",radians_rot,transwidht,transheight);

       rsMatrixMultiply(&matrixa,&matrixb);//平移
       rsMatrixLoadScale(&matrixe,2.0f,2.0f,0);
}

uchar4 __attribute__((kernel)) xxxinvert(uchar4 in, uint32_t x, uint32_t y) {
        uchar4 out = in;
        float4 pointA;
        pointA.x=x;
        pointA.y=y;
        pointA.z=1;
        pointA.w=1;

        float4 changePointA= rsMatrixMultiply(&matrixa,pointA);//平移

        //float4 changePointB= rsMatrixMultiply(&matrixa,changePointA);//旋转
        float4 changePointC= rsMatrixMultiply(&matrixc,changePointA);//平移
        float4 changePointD= rsMatrixMultiply(&matrixe,changePointC);//

        rsSetElementAt_uchar4(gIntFrame,out,(int)changePointD.x,(int)changePointD.y);
        return out;
}
