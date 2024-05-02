using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CubeMovement : MonoBehaviour
{
    float speed = 0.25f;

    // Update is called once per frame
    void Update()
    {
        float hVal = Input.GetAxis("Horizontal");
        float vVal = Input.GetAxis("Vertical");

        //transform.Translate(x,y,z)
        transform.Translate((speed * hVal), (speed * vVal), 0.0f);
    }
}
